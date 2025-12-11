package bbw.ch.FairwayEcoBackend.service;

import bbw.ch.FairwayEcoBackend.dto.GolfBallCreateDto;
import bbw.ch.FairwayEcoBackend.dto.GolfBallResponseDto;
import bbw.ch.FairwayEcoBackend.exception.InsufficientStockException;
import bbw.ch.FairwayEcoBackend.exception.ResourceNotFoundException;
import bbw.ch.FairwayEcoBackend.mapper.GolfBallMapper;
import bbw.ch.FairwayEcoBackend.model.BallCondition;
import bbw.ch.FairwayEcoBackend.model.GolfBall;
import bbw.ch.FairwayEcoBackend.repository.GolfBallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of GolfBallService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GolfBallServiceImpl implements GolfBallService {

  private final GolfBallRepository golfBallRepository;
  private final GolfBallMapper golfBallMapper;

  @Override
  public GolfBallResponseDto createGolfBall(GolfBallCreateDto dto) {
    log.info("Creating new golf ball: {} {}", dto.getBrand(), dto.getModel());
    GolfBall golfBall = golfBallMapper.toEntity(dto);
    GolfBall saved = golfBallRepository.save(golfBall);
    log.info("Created golf ball with ID: {}", saved.getId());
    return golfBallMapper.toResponseDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public GolfBallResponseDto getGolfBallById(Long id) {
    GolfBall golfBall = findGolfBallById(id);
    return golfBallMapper.toResponseDto(golfBall);
  }

  @Override
  @Transactional(readOnly = true)
  public List<GolfBallResponseDto> getAllGolfBalls() {
    return golfBallRepository.findAll().stream()
        .map(golfBallMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<GolfBallResponseDto> getAvailableGolfBalls() {
    return golfBallRepository.findAllAvailableOrderByPriceAsc().stream()
        .map(golfBallMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<GolfBallResponseDto> getGolfBallsByBrand(String brand) {
    return golfBallRepository.findByBrandIgnoreCase(brand).stream()
        .map(golfBallMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<GolfBallResponseDto> getGolfBallsByCondition(BallCondition condition) {
    return golfBallRepository.findByCondition(condition).stream()
        .map(golfBallMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<GolfBallResponseDto> getGolfBallsByFilters(String brand, BallCondition condition,
      BigDecimal minPrice, BigDecimal maxPrice) {
    return golfBallRepository.findByFilters(brand, condition, minPrice, maxPrice).stream()
        .map(golfBallMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<String> getAllBrands() {
    return golfBallRepository.findAllBrands();
  }

  @Override
  public GolfBallResponseDto updateGolfBall(Long id, GolfBallCreateDto dto) {
    log.info("Updating golf ball with ID: {}", id);
    GolfBall golfBall = findGolfBallById(id);
    golfBallMapper.updateEntityFromDto(dto, golfBall);
    GolfBall saved = golfBallRepository.save(golfBall);
    log.info("Updated golf ball with ID: {}", saved.getId());
    return golfBallMapper.toResponseDto(saved);
  }

  @Override
  public void deleteGolfBall(Long id) {
    log.info("Deleting golf ball with ID: {}", id);
    GolfBall golfBall = findGolfBallById(id);
    golfBallRepository.delete(golfBall);
    log.info("Deleted golf ball with ID: {}", id);
  }

  @Override
  public void updateStock(Long id, Integer quantityChange) {
    GolfBall golfBall = findGolfBallById(id);
    int newQuantity = golfBall.getQuantity() + quantityChange;
    if (newQuantity < 0) {
      throw new InsufficientStockException(id, Math.abs(quantityChange), golfBall.getQuantity());
    }
    golfBall.setQuantity(newQuantity);
    golfBallRepository.save(golfBall);
    log.info("Updated stock for golf ball {}: {} -> {}", id, golfBall.getQuantity() - quantityChange, newQuantity);
  }

  private GolfBall findGolfBallById(Long id) {
    return golfBallRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("GolfBall", "id", id));
  }
}
