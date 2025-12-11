package bbw.ch.FairwayEcoBackend.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event representing an order cancellation.
 * Published to Kafka for other microservices to consume.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelledEvent {

  private Long orderId;
  private Long customerId;
  private String customerEmail;
  private BigDecimal refundAmount;
  private LocalDateTime cancelledAt;
}
