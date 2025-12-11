package bbw.ch.FairwayEcoBackend.dto;

import bbw.ch.FairwayEcoBackend.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating order status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateDto {

  @NotNull(message = "Status is required")
  private OrderStatus status;
}
