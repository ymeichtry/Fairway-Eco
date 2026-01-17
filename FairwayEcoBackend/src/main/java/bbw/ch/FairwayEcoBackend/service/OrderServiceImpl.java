package bbw.ch.FairwayEcoBackend.service;

import bbw.ch.FairwayEcoBackend.dto.OrderCreateDto;
import bbw.ch.FairwayEcoBackend.dto.OrderItemDto;
import bbw.ch.FairwayEcoBackend.dto.OrderResponseDto;
import bbw.ch.FairwayEcoBackend.event.OrderEventPublisher;
import bbw.ch.FairwayEcoBackend.exception.InsufficientStockException;
import bbw.ch.FairwayEcoBackend.exception.InvalidOrderStateException;
import bbw.ch.FairwayEcoBackend.exception.ResourceNotFoundException;
import bbw.ch.FairwayEcoBackend.mapper.CustomerMapper;
import bbw.ch.FairwayEcoBackend.mapper.OrderMapper;
import bbw.ch.FairwayEcoBackend.model.*;
import bbw.ch.FairwayEcoBackend.repository.CustomerRepository;
import bbw.ch.FairwayEcoBackend.repository.GolfBallRepository;
import bbw.ch.FairwayEcoBackend.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final GolfBallRepository golfBallRepository;
  private final OrderMapper orderMapper;
  private final CustomerMapper customerMapper;
  private final OrderEventPublisher orderEventPublisher;

  @Override
  @CircuitBreaker(name = "orderService", fallbackMethod = "createOrderFallback")
  public OrderResponseDto createOrder(OrderCreateDto dto) {
    log.info("Creating new order for customer ID: {}", dto.getCustomerId());

    // Find customer
    Customer customer = customerRepository.findById(dto.getCustomerId())
        .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", dto.getCustomerId()));

    // Create order
    Order order = Order.builder()
        .customer(customer)
        .status(OrderStatus.PENDING)
        .shippingAddress(dto.getShippingAddress() != null
            ? customerMapper.toAddressEntity(dto.getShippingAddress())
            : customer.getAddress())
        .build();

    // Process order items
    for (OrderItemDto itemDto : dto.getItems()) {
      GolfBall golfBall = golfBallRepository.findById(itemDto.getGolfBallId())
          .orElseThrow(() -> new ResourceNotFoundException("GolfBall", "id", itemDto.getGolfBallId()));

      // Check stock
      if (golfBall.getQuantity() < itemDto.getQuantity()) {
        throw new InsufficientStockException(golfBall.getId(), itemDto.getQuantity(), golfBall.getQuantity());
      }

      // Reduce stock
      golfBall.setQuantity(golfBall.getQuantity() - itemDto.getQuantity());
      golfBallRepository.save(golfBall);

      // Create order item
      OrderItem orderItem = OrderItem.builder()
          .golfBall(golfBall)
          .quantity(itemDto.getQuantity())
          .unitPrice(golfBall.getPrice())
          .build();

      order.addItem(orderItem);
    }

    Order saved = orderRepository.save(order);
    log.info("Created order with ID: {}", saved.getId());

    // Publish order created event
    orderEventPublisher.publishOrderCreated(saved);

    return orderMapper.toResponseDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  @CircuitBreaker(name = "orderService", fallbackMethod = "getOrderByIdFallback")
  public OrderResponseDto getOrderById(Long id) {
    Order order = findOrderById(id);
    return orderMapper.toResponseDto(order);
  }

  @Override
  @Transactional(readOnly = true)
  @CircuitBreaker(name = "orderService", fallbackMethod = "getAllOrdersFallback")
  public List<OrderResponseDto> getAllOrders() {
    return orderRepository.findAll().stream()
        .map(orderMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  @CircuitBreaker(name = "orderService", fallbackMethod = "getOrdersByCustomerIdFallback")
  public List<OrderResponseDto> getOrdersByCustomerId(Long customerId) {
    return orderRepository.findByCustomerId(customerId).stream()
        .map(orderMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  @CircuitBreaker(name = "orderService", fallbackMethod = "getOrdersByStatusFallback")
  public List<OrderResponseDto> getOrdersByStatus(OrderStatus status) {
    return orderRepository.findByStatus(status).stream()
        .map(orderMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @CircuitBreaker(name = "orderService", fallbackMethod = "updateOrderStatusFallback")
  public OrderResponseDto updateOrderStatus(Long id, OrderStatus newStatus) {
    log.info("Updating order {} status to: {}", id, newStatus);
    Order order = findOrderById(id);

    validateStatusTransition(order.getStatus(), newStatus);

    OrderStatus oldStatus = order.getStatus();
    order.setStatus(newStatus);
    Order saved = orderRepository.save(order);

    // Publish status changed event
    orderEventPublisher.publishOrderStatusChanged(saved, oldStatus, newStatus);

    log.info("Updated order {} status from {} to {}", id, oldStatus, newStatus);
    return orderMapper.toResponseDto(saved);
  }

  @Override
  @CircuitBreaker(name = "orderService", fallbackMethod = "cancelOrderFallback")
  public void cancelOrder(Long id) {
    log.info("Cancelling order with ID: {}", id);
    Order order = findOrderById(id);

    if (order.getStatus() == OrderStatus.SHIPPED ||
        order.getStatus() == OrderStatus.DELIVERED) {
      throw new InvalidOrderStateException(
          "Cannot cancel order that has been " + order.getStatus().getDescription().toLowerCase());
    }

    // Restore stock for cancelled items
    for (OrderItem item : order.getItems()) {
      GolfBall golfBall = item.getGolfBall();
      golfBall.setQuantity(golfBall.getQuantity() + item.getQuantity());
      golfBallRepository.save(golfBall);
    }

    order.setStatus(OrderStatus.CANCELLED);
    orderRepository.save(order);

    // Publish order cancelled event
    orderEventPublisher.publishOrderCancelled(order);

    log.info("Cancelled order with ID: {}", id);
  }

  private Order findOrderById(Long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
  }

  private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
    // Define valid transitions
    switch (currentStatus) {
      case PENDING:
        if (newStatus != OrderStatus.PAID && newStatus != OrderStatus.CANCELLED) {
          throw new InvalidOrderStateException(
              "Cannot transition from PENDING to " + newStatus);
        }
        break;
      case PAID:
        if (newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.CANCELLED) {
          throw new InvalidOrderStateException(
              "Cannot transition from PAID to " + newStatus);
        }
        break;
      case PROCESSING:
        if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
          throw new InvalidOrderStateException(
              "Cannot transition from PROCESSING to " + newStatus);
        }
        break;
      case SHIPPED:
        if (newStatus != OrderStatus.DELIVERED) {
          throw new InvalidOrderStateException(
              "Cannot transition from SHIPPED to " + newStatus);
        }
        break;
      case DELIVERED:
        if (newStatus != OrderStatus.REFUNDED) {
          throw new InvalidOrderStateException(
              "Cannot transition from DELIVERED to " + newStatus);
        }
        break;
      case CANCELLED:
      case REFUNDED:
        throw new InvalidOrderStateException(
            "Cannot transition from " + currentStatus + " to any other status");
    }
  }

  // Fallback methods
  private OrderResponseDto createOrderFallback(OrderCreateDto dto, Exception ex) {
    log.error("Circuit breaker fallback: Failed to create order for customer ID: {}", dto.getCustomerId(), ex);
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
  }

  private OrderResponseDto getOrderByIdFallback(Long id, Exception ex) {
    log.error("Circuit breaker fallback: Failed to get order with ID: {}", id, ex);
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
  }

  private List<OrderResponseDto> getAllOrdersFallback(Exception ex) {
    log.error("Circuit breaker fallback: Failed to get all orders", ex);
    return new ArrayList<>();
  }

  private List<OrderResponseDto> getOrdersByCustomerIdFallback(Long customerId, Exception ex) {
    log.error("Circuit breaker fallback: Failed to get orders for customer ID: {}", customerId, ex);
    return new ArrayList<>();
  }

  private List<OrderResponseDto> getOrdersByStatusFallback(OrderStatus status, Exception ex) {
    log.error("Circuit breaker fallback: Failed to get orders by status: {}", status, ex);
    return new ArrayList<>();
  }

  private OrderResponseDto updateOrderStatusFallback(Long id, OrderStatus newStatus, Exception ex) {
    log.error("Circuit breaker fallback: Failed to update order {} status to: {}", id, newStatus, ex);
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
  }

  private void cancelOrderFallback(Long id, Exception ex) {
    log.error("Circuit breaker fallback: Failed to cancel order with ID: {}", id, ex);
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
  }
}
