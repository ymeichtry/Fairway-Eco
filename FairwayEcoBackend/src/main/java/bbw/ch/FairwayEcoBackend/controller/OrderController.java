package bbw.ch.FairwayEcoBackend.controller;

import bbw.ch.FairwayEcoBackend.dto.OrderCreateDto;
import bbw.ch.FairwayEcoBackend.dto.OrderResponseDto;
import bbw.ch.FairwayEcoBackend.dto.OrderStatusUpdateDto;
import bbw.ch.FairwayEcoBackend.model.OrderStatus;
import bbw.ch.FairwayEcoBackend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Order operations.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponseDto> createOrder(
      @Valid @RequestBody OrderCreateDto dto) {
    OrderResponseDto created = orderService.createOrder(dto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  @GetMapping
  public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<OrderResponseDto>> getOrdersByCustomerId(
      @PathVariable Long customerId) {
    return ResponseEntity.ok(orderService.getOrdersByCustomerId(customerId));
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<List<OrderResponseDto>> getOrdersByStatus(
      @PathVariable OrderStatus status) {
    return ResponseEntity.ok(orderService.getOrdersByStatus(status));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<OrderResponseDto> updateOrderStatus(
      @PathVariable Long id,
      @Valid @RequestBody OrderStatusUpdateDto dto) {
    return ResponseEntity.ok(orderService.updateOrderStatus(id, dto.getStatus()));
  }

  @PostMapping("/{id}/cancel")
  public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
    orderService.cancelOrder(id);
    return ResponseEntity.ok().build();
  }
}
