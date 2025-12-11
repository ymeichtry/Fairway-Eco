package bbw.ch.FairwayEcoBackend.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Listener for order-related events from Kafka.
 * This can be extended to handle events from other microservices.
 */
@Component
@Slf4j
public class OrderEventListener {

  @KafkaListener(topics = "order-created", groupId = "${spring.kafka.consumer.group-id}")
  public void handleOrderCreated(OrderCreatedEvent event) {
    log.info("Received OrderCreatedEvent: orderId={}, customer={}, total={}",
        event.getOrderId(), event.getCustomerName(), event.getTotalAmount());

    // Here you can add logic to:
    // - Send confirmation email
    // - Update inventory in other systems
    // - Trigger warehouse notification
    // - etc.
  }

  @KafkaListener(topics = "order-status-changed", groupId = "${spring.kafka.consumer.group-id}")
  public void handleOrderStatusChanged(OrderStatusChangedEvent event) {
    log.info("Received OrderStatusChangedEvent: orderId={}, {} -> {}",
        event.getOrderId(), event.getPreviousStatus(), event.getNewStatus());

    // Here you can add logic to:
    // - Send status update email
    // - Update tracking systems
    // - Trigger shipping notifications
    // - etc.
  }

  @KafkaListener(topics = "order-cancelled", groupId = "${spring.kafka.consumer.group-id}")
  public void handleOrderCancelled(OrderCancelledEvent event) {
    log.info("Received OrderCancelledEvent: orderId={}, refund={}",
        event.getOrderId(), event.getRefundAmount());

    // Here you can add logic to:
    // - Process refund
    // - Send cancellation email
    // - Update inventory
    // - etc.
  }
}
