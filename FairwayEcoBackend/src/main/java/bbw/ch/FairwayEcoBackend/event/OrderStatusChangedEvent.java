package bbw.ch.FairwayEcoBackend.event;

import bbw.ch.FairwayEcoBackend.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event representing an order status change.
 * Published to Kafka for other microservices to consume.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangedEvent {

  private Long orderId;
  private Long customerId;
  private String customerEmail;
  private OrderStatus previousStatus;
  private OrderStatus newStatus;
  private LocalDateTime changedAt;
}
