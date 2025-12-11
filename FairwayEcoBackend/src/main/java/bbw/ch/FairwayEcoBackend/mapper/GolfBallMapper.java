package bbw.ch.FairwayEcoBackend.mapper;

import bbw.ch.FairwayEcoBackend.dto.GolfBallCreateDto;
import bbw.ch.FairwayEcoBackend.dto.GolfBallResponseDto;
import bbw.ch.FairwayEcoBackend.model.GolfBall;
import org.springframework.stereotype.Component;

/**
 * Mapper for GolfBall entity and DTOs.
 */
@Component
public class GolfBallMapper {

  public GolfBall toEntity(GolfBallCreateDto dto) {
    return GolfBall.builder()
        .brand(dto.getBrand())
        .model(dto.getModel())
        .price(dto.getPrice())
        .quantity(dto.getQuantity())
        .condition(dto.getCondition())
        .description(dto.getDescription())
        .imageUrl(dto.getImageUrl())
        .build();
  }

  public GolfBallResponseDto toResponseDto(GolfBall entity) {
    return GolfBallResponseDto.builder()
        .id(entity.getId())
        .brand(entity.getBrand())
        .model(entity.getModel())
        .price(entity.getPrice())
        .quantity(entity.getQuantity())
        .condition(entity.getCondition())
        .conditionDescription(entity.getCondition().getDescription())
        .description(entity.getDescription())
        .imageUrl(entity.getImageUrl())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  public void updateEntityFromDto(GolfBallCreateDto dto, GolfBall entity) {
    entity.setBrand(dto.getBrand());
    entity.setModel(dto.getModel());
    entity.setPrice(dto.getPrice());
    entity.setQuantity(dto.getQuantity());
    entity.setCondition(dto.getCondition());
    entity.setDescription(dto.getDescription());
    entity.setImageUrl(dto.getImageUrl());
  }
}
