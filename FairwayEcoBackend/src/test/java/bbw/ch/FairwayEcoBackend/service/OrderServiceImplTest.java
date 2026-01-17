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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Order Service Tests")
class OrderServiceImplTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private GolfBallRepository golfBallRepository;

  @Mock
  private OrderMapper orderMapper;

  @Mock
  private CustomerMapper customerMapper;

  @Mock
  private OrderEventPublisher orderEventPublisher;

  @InjectMocks
  private OrderServiceImpl orderService;

  private Order testOrder;
  private Customer testCustomer;
  private GolfBall testGolfBall;
  private OrderCreateDto testCreateDto;
  private OrderResponseDto testResponseDto;

  @BeforeEach
  void setUp() {
    testCustomer = Customer.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .email("john.doe@example.com")
        .address(Address.builder()
            .street("123 Golf Lane")
            .city("Golftown")
            .postalCode("12345")
            .country("USA")
            .build())
        .build();

    testGolfBall = GolfBall.builder()
        .id(1L)
        .brand("Titleist")
        .model("Pro V1")
        .quantity(100)
        .price(BigDecimal.valueOf(29.99))
        .build();

    testOrder = Order.builder()
        .id(1L)
        .customer(testCustomer)
        .status(OrderStatus.PENDING)
        .shippingAddress(testCustomer.getAddress())
        .build();

    OrderItemDto itemDto = new OrderItemDto();
    itemDto.setGolfBallId(1L);
    itemDto.setQuantity(10);

    testCreateDto = new OrderCreateDto();
    testCreateDto.setCustomerId(1L);
    testCreateDto.setItems(Arrays.asList(itemDto));

    testResponseDto = new OrderResponseDto();
    testResponseDto.setId(1L);
    testResponseDto.setStatus(OrderStatus.PENDING);
  }

  @Test
  @DisplayName("Should create order successfully")
  void testCreateOrder() {
    // Given
    when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
    when(golfBallRepository.findById(1L)).thenReturn(Optional.of(testGolfBall));
    when(golfBallRepository.save(any(GolfBall.class))).thenReturn(testGolfBall);
    when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
    when(orderMapper.toResponseDto(any(Order.class))).thenReturn(testResponseDto);
    doNothing().when(orderEventPublisher).publishOrderCreated(any(Order.class));

    // When
    OrderResponseDto result = orderService.createOrder(testCreateDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(orderRepository, times(1)).save(any(Order.class));
    verify(orderEventPublisher, times(1)).publishOrderCreated(any(Order.class));
  }

  @Test
  @DisplayName("Should throw exception when customer not found during order creation")
  void testCreateOrderCustomerNotFound() {
    // Given
    when(customerRepository.findById(999L)).thenReturn(Optional.empty());
    testCreateDto.setCustomerId(999L);

    // When & Then
    assertThatThrownBy(() -> orderService.createOrder(testCreateDto))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Customer");
    verify(orderRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should throw exception when golf ball not found during order creation")
  void testCreateOrderGolfBallNotFound() {
    // Given
    when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
    when(golfBallRepository.findById(999L)).thenReturn(Optional.empty());

    OrderItemDto itemDto = new OrderItemDto();
    itemDto.setGolfBallId(999L);
    itemDto.setQuantity(10);
    testCreateDto.setItems(Arrays.asList(itemDto));

    // When & Then
    assertThatThrownBy(() -> orderService.createOrder(testCreateDto))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("GolfBall");
    verify(orderRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should throw exception when insufficient stock")
  void testCreateOrderInsufficientStock() {
    // Given
    testGolfBall.setQuantity(5);
    when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
    when(golfBallRepository.findById(1L)).thenReturn(Optional.of(testGolfBall));

    // When & Then
    assertThatThrownBy(() -> orderService.createOrder(testCreateDto))
        .isInstanceOf(InsufficientStockException.class);
    verify(orderRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should get order by ID")
  void testGetOrderById() {
    // Given
    when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
    when(orderMapper.toResponseDto(testOrder)).thenReturn(testResponseDto);

    // When
    OrderResponseDto result = orderService.getOrderById(1L);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(orderRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should throw exception when order not found")
  void testGetOrderByIdNotFound() {
    // Given
    when(orderRepository.findById(999L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> orderService.getOrderById(999L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Order");
  }

  @Test
  @DisplayName("Should get all orders")
  void testGetAllOrders() {
    // Given
    Order order2 = Order.builder().id(2L).status(OrderStatus.PAID).build();
    List<Order> orders = Arrays.asList(testOrder, order2);
    when(orderRepository.findAll()).thenReturn(orders);
    when(orderMapper.toResponseDto(any(Order.class))).thenReturn(testResponseDto);

    // When
    List<OrderResponseDto> result = orderService.getAllOrders();

    // Then
    assertThat(result).hasSize(2);
    verify(orderRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should get orders by customer ID")
  void testGetOrdersByCustomerId() {
    // Given
    List<Order> orders = Arrays.asList(testOrder);
    when(orderRepository.findByCustomerId(1L)).thenReturn(orders);
    when(orderMapper.toResponseDto(testOrder)).thenReturn(testResponseDto);

    // When
    List<OrderResponseDto> result = orderService.getOrdersByCustomerId(1L);

    // Then
    assertThat(result).hasSize(1);
    verify(orderRepository, times(1)).findByCustomerId(1L);
  }

  @Test
  @DisplayName("Should get orders by status")
  void testGetOrdersByStatus() {
    // Given
    List<Order> orders = Arrays.asList(testOrder);
    when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(orders);
    when(orderMapper.toResponseDto(testOrder)).thenReturn(testResponseDto);

    // When
    List<OrderResponseDto> result = orderService.getOrdersByStatus(OrderStatus.PENDING);

    // Then
    assertThat(result).hasSize(1);
    verify(orderRepository, times(1)).findByStatus(OrderStatus.PENDING);
  }

  @Test
  @DisplayName("Should update order status successfully")
  void testUpdateOrderStatus() {
    // Given
    when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
    when(orderRepository.save(testOrder)).thenReturn(testOrder);
    when(orderMapper.toResponseDto(testOrder)).thenReturn(testResponseDto);
    doNothing().when(orderEventPublisher).publishOrderStatusChanged(
        any(Order.class), any(OrderStatus.class), any(OrderStatus.class));

    // When
    OrderResponseDto result = orderService.updateOrderStatus(1L, OrderStatus.PAID);

    // Then
    assertThat(result).isNotNull();
    verify(orderRepository, times(1)).save(testOrder);
    verify(orderEventPublisher, times(1)).publishOrderStatusChanged(
        any(Order.class), eq(OrderStatus.PENDING), eq(OrderStatus.PAID));
  }

  @Test
  @DisplayName("Should throw exception for invalid status transition")
  void testUpdateOrderStatusInvalidTransition() {
    // Given
    testOrder.setStatus(OrderStatus.DELIVERED);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

    // When & Then
    assertThatThrownBy(() -> orderService.updateOrderStatus(1L, OrderStatus.PENDING))
        .isInstanceOf(InvalidOrderStateException.class);
    verify(orderRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should cancel order successfully")
  void testCancelOrder() {
    // Given
    OrderItem orderItem = OrderItem.builder()
        .golfBall(testGolfBall)
        .quantity(10)
        .unitPrice(BigDecimal.valueOf(29.99))
        .build();
    testOrder.addItem(orderItem);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
    when(golfBallRepository.save(any(GolfBall.class))).thenReturn(testGolfBall);
    when(orderRepository.save(testOrder)).thenReturn(testOrder);
    doNothing().when(orderEventPublisher).publishOrderCancelled(any(Order.class));

    // When
    orderService.cancelOrder(1L);

    // Then
    assertThat(testOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    verify(golfBallRepository, times(1)).save(testGolfBall);
    verify(orderEventPublisher, times(1)).publishOrderCancelled(testOrder);
  }

  @Test
  @DisplayName("Should throw exception when cancelling shipped order")
  void testCancelShippedOrder() {
    // Given
    testOrder.setStatus(OrderStatus.SHIPPED);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

    // When & Then
    assertThatThrownBy(() -> orderService.cancelOrder(1L))
        .isInstanceOf(InvalidOrderStateException.class)
        .hasMessageContaining("Cannot cancel");
    verify(orderRepository, never()).save(any());
  }
}
