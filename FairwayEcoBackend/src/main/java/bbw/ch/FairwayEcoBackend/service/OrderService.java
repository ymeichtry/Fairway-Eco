package bbw.ch.FairwayEcoBackend.service;

import bbw.ch.FairwayEcoBackend.dto.OrderCreateDto;
import bbw.ch.FairwayEcoBackend.dto.OrderResponseDto;
import bbw.ch.FairwayEcoBackend.model.OrderStatus;

import java.util.List;

/**
 * Service interface for Order operations.
 */
public interface OrderService {

  OrderResponseDto createOrder(OrderCreateDto dto);

  OrderResponseDto getOrderById(Long id);

  List<OrderResponseDto> getAllOrders();

  List<OrderResponseDto> getOrdersByCustomerId(Long customerId);

  List<OrderResponseDto> getOrdersByStatus(OrderStatus status);

  OrderResponseDto updateOrderStatus(Long id, OrderStatus newStatus);

  void cancelOrder(Long id);
}
