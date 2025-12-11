package bbw.ch.FairwayEcoBackend.dto;

import bbw.ch.FairwayEcoBackend.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for returning order information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

  private Long id;
  private Long customerId;
  private String customerName;
  private List<OrderItemResponseDto> items;
  private OrderStatus status;
  private String statusDescription;
  private BigDecimal totalAmount;
  private AddressDto shippingAddress;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
