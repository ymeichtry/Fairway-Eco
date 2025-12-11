package bbw.ch.FairwayEcoBackend.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Event representing an order creation.
 * Published to Kafka for other microservices to consume.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

  private Long orderId;
  private Long customerId;
  private String customerEmail;
  private String customerName;
  private BigDecimal totalAmount;
  private List<OrderItemEvent> items;
  private LocalDateTime createdAt;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderItemEvent {
    private Long golfBallId;
    private String brand;
    private String model;
    private Integer quantity;
    private BigDecimal unitPrice;
  }
}
