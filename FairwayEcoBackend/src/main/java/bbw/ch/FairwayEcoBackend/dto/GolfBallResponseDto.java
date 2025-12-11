package bbw.ch.FairwayEcoBackend.dto;

import bbw.ch.FairwayEcoBackend.model.BallCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning golf ball information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GolfBallResponseDto {

  private Long id;
  private String brand;
  private String model;
  private BigDecimal price;
  private Integer quantity;
  private BallCondition condition;
  private String conditionDescription;
  private String description;
  private String imageUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
