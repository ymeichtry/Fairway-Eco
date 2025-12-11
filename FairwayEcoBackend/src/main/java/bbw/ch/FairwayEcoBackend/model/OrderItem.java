package bbw.ch.FairwayEcoBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entity representing an item within an order.
 */
@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Order is required")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @NotNull(message = "Golf ball is required")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "golf_ball_id", nullable = false)
  private GolfBall golfBall;

  @NotNull(message = "Quantity is required")
  @Min(value = 1, message = "Quantity must be at least 1")
  @Column(nullable = false)
  private Integer quantity;

  @NotNull(message = "Unit price is required")
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal unitPrice;

  public BigDecimal getSubtotal() {
    return unitPrice.multiply(BigDecimal.valueOf(quantity));
  }
}
