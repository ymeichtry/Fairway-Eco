package bbw.ch.FairwayEcoBackend.dto;

import bbw.ch.FairwayEcoBackend.model.BallCondition;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new golf ball.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GolfBallCreateDto {

  @NotBlank(message = "Brand is required")
  @Size(max = 100)
  private String brand;

  @NotBlank(message = "Model is required")
  @Size(max = 100)
  private String model;

  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.01", message = "Price must be greater than 0")
  private BigDecimal price;

  @NotNull(message = "Quantity is required")
  @Min(value = 0, message = "Quantity cannot be negative")
  private Integer quantity;

  @NotNull(message = "Condition is required")
  private BallCondition condition;

  @Size(max = 500)
  private String description;

  @Size(max = 255)
  private String imageUrl;
}
