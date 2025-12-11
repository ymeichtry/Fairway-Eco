package bbw.ch.FairwayEcoBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating a new order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

  @NotNull(message = "Customer ID is required")
  private Long customerId;

  @NotEmpty(message = "Order must have at least one item")
  @Valid
  private List<OrderItemDto> items;

  @Valid
  private AddressDto shippingAddress;
}
