package bbw.ch.FairwayEcoBackend.event;

import bbw.ch.FairwayEcoBackend.model.Order;
import bbw.ch.FairwayEcoBackend.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Publisher for order-related events to Kafka.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  private static final String ORDER_CREATED_TOPIC = "order-created";
  private static final String ORDER_STATUS_CHANGED_TOPIC = "order-status-changed";
  private static final String ORDER_CANCELLED_TOPIC = "order-cancelled";

  public void publishOrderCreated(Order order) {
    OrderCreatedEvent event = OrderCreatedEvent.builder()
        .orderId(order.getId())
        .customerId(order.getCustomer().getId())
        .customerEmail(order.getCustomer().getEmail())
        .customerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName())
        .totalAmount(order.getTotalAmount())
        .items(order.getItems().stream()
            .map(item -> OrderCreatedEvent.OrderItemEvent.builder()
                .golfBallId(item.getGolfBall().getId())
                .brand(item.getGolfBall().getBrand())
                .model(item.getGolfBall().getModel())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .build())
            .collect(Collectors.toList()))
        .createdAt(order.getCreatedAt())
        .build();

    kafkaTemplate.send(ORDER_CREATED_TOPIC, order.getId().toString(), event);
    log.info("Published OrderCreatedEvent for order ID: {}", order.getId());
  }

  public void publishOrderStatusChanged(Order order, OrderStatus previousStatus, OrderStatus newStatus) {
    OrderStatusChangedEvent event = OrderStatusChangedEvent.builder()
        .orderId(order.getId())
        .customerId(order.getCustomer().getId())
        .customerEmail(order.getCustomer().getEmail())
        .previousStatus(previousStatus)
        .newStatus(newStatus)
        .changedAt(LocalDateTime.now())
        .build();

    kafkaTemplate.send(ORDER_STATUS_CHANGED_TOPIC, order.getId().toString(), event);
    log.info("Published OrderStatusChangedEvent for order ID: {} ({} -> {})",
        order.getId(), previousStatus, newStatus);
  }

  public void publishOrderCancelled(Order order) {
    OrderCancelledEvent event = OrderCancelledEvent.builder()
        .orderId(order.getId())
        .customerId(order.getCustomer().getId())
        .customerEmail(order.getCustomer().getEmail())
        .refundAmount(order.getTotalAmount())
        .cancelledAt(LocalDateTime.now())
        .build();

    kafkaTemplate.send(ORDER_CANCELLED_TOPIC, order.getId().toString(), event);
    log.info("Published OrderCancelledEvent for order ID: {}", order.getId());
  }
}
