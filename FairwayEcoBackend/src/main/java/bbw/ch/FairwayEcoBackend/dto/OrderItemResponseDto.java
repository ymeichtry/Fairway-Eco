package bbw.ch.FairwayEcoBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for returning order item information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {

  private Long id;
  private Long golfBallId;
  private String golfBallBrand;
  private String golfBallModel;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal subtotal;
}
