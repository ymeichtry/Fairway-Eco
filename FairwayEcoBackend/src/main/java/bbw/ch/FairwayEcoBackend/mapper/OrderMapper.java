package bbw.ch.FairwayEcoBackend.mapper;

import bbw.ch.FairwayEcoBackend.dto.OrderItemResponseDto;
import bbw.ch.FairwayEcoBackend.dto.OrderResponseDto;
import bbw.ch.FairwayEcoBackend.model.Order;
import bbw.ch.FairwayEcoBackend.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper for Order entity and DTOs.
 */
@Component
@RequiredArgsConstructor
public class OrderMapper {

  private final CustomerMapper customerMapper;

  public OrderResponseDto toResponseDto(Order entity) {
    return OrderResponseDto.builder()
        .id(entity.getId())
        .customerId(entity.getCustomer().getId())
        .customerName(entity.getCustomer().getFirstName() + " " + entity.getCustomer().getLastName())
        .items(entity.getItems().stream()
            .map(this::toOrderItemResponseDto)
            .collect(Collectors.toList()))
        .status(entity.getStatus())
        .statusDescription(entity.getStatus().getDescription())
        .totalAmount(entity.getTotalAmount())
        .shippingAddress(customerMapper.toAddressDto(entity.getShippingAddress()))
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  public OrderItemResponseDto toOrderItemResponseDto(OrderItem item) {
    return OrderItemResponseDto.builder()
        .id(item.getId())
        .golfBallId(item.getGolfBall().getId())
        .golfBallBrand(item.getGolfBall().getBrand())
        .golfBallModel(item.getGolfBall().getModel())
        .quantity(item.getQuantity())
        .unitPrice(item.getUnitPrice())
        .subtotal(item.getSubtotal())
        .build();
  }
}
