package bbw.ch.FairwayEcoBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a golf ball product.
 * These are recycled golf balls recovered from water hazards.
 */
@Entity
@Table(name = "golf_balls")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GolfBall {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Brand is required")
  @Size(max = 100)
  @Column(nullable = false)
  private String brand;

  @NotBlank(message = "Model is required")
  @Size(max = 100)
  @Column(nullable = false)
  private String model;

  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.01", message = "Price must be greater than 0")
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @NotNull(message = "Quantity is required")
  @Min(value = 0, message = "Quantity cannot be negative")
  @Column(nullable = false)
  private Integer quantity;

  @NotNull(message = "Condition is required")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BallCondition condition;

  @Size(max = 500)
  private String description;

  @Size(max = 255)
  private String imageUrl;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
