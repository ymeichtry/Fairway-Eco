package bbw.ch.FairwayEcoBackend.service;

import bbw.ch.FairwayEcoBackend.dto.GolfBallCreateDto;
import bbw.ch.FairwayEcoBackend.dto.GolfBallResponseDto;
import bbw.ch.FairwayEcoBackend.model.BallCondition;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for GolfBall operations.
 */
public interface GolfBallService {

  GolfBallResponseDto createGolfBall(GolfBallCreateDto dto);

  GolfBallResponseDto getGolfBallById(Long id);

  List<GolfBallResponseDto> getAllGolfBalls();

  List<GolfBallResponseDto> getAvailableGolfBalls();

  List<GolfBallResponseDto> getGolfBallsByBrand(String brand);

  List<GolfBallResponseDto> getGolfBallsByCondition(BallCondition condition);

  List<GolfBallResponseDto> getGolfBallsByFilters(String brand, BallCondition condition,
      BigDecimal minPrice, BigDecimal maxPrice);

  List<String> getAllBrands();

  GolfBallResponseDto updateGolfBall(Long id, GolfBallCreateDto dto);

  void deleteGolfBall(Long id);

  void updateStock(Long id, Integer quantityChange);
}
