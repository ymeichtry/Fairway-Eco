package bbw.ch.FairwayEcoBackend.service;

import bbw.ch.FairwayEcoBackend.dto.GolfBallCreateDto;
import bbw.ch.FairwayEcoBackend.dto.GolfBallResponseDto;
import bbw.ch.FairwayEcoBackend.exception.InsufficientStockException;
import bbw.ch.FairwayEcoBackend.exception.ResourceNotFoundException;
import bbw.ch.FairwayEcoBackend.mapper.GolfBallMapper;
import bbw.ch.FairwayEcoBackend.model.BallCondition;
import bbw.ch.FairwayEcoBackend.model.GolfBall;
import bbw.ch.FairwayEcoBackend.repository.GolfBallRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GolfBallServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GolfBall Service Tests")
class GolfBallServiceImplTest {

  @Mock
  private GolfBallRepository golfBallRepository;

  @Mock
  private GolfBallMapper golfBallMapper;

  @InjectMocks
  private GolfBallServiceImpl golfBallService;

  private GolfBall testGolfBall;
  private GolfBallCreateDto testCreateDto;
  private GolfBallResponseDto testResponseDto;

  @BeforeEach
  void setUp() {
    testGolfBall = GolfBall.builder()
        .id(1L)
        .brand("Titleist")
        .model("Pro V1")
        .condition(BallCondition.MINT)
        .quantity(100)
        .price(BigDecimal.valueOf(29.99))
        .description("Premium golf ball")
        .build();

    testCreateDto = new GolfBallCreateDto();
    testCreateDto.setBrand("Titleist");
    testCreateDto.setModel("Pro V1");
    testCreateDto.setCondition(BallCondition.MINT);
    testCreateDto.setQuantity(100);
    testCreateDto.setPrice(BigDecimal.valueOf(29.99));
    testCreateDto.setDescription("Premium golf ball");

    testResponseDto = new GolfBallResponseDto();
    testResponseDto.setId(1L);
    testResponseDto.setBrand("Titleist");
    testResponseDto.setModel("Pro V1");
    testResponseDto.setCondition(BallCondition.MINT);
    testResponseDto.setQuantity(100);
    testResponseDto.setPrice(BigDecimal.valueOf(29.99));
  }

  @Test
  @DisplayName("Should create golf ball successfully")
  void testCreateGolfBall() {
    // Given
    when(golfBallMapper.toEntity(testCreateDto)).thenReturn(testGolfBall);
    when(golfBallRepository.save(any(GolfBall.class))).thenReturn(testGolfBall);
    when(golfBallMapper.toResponseDto(testGolfBall)).thenReturn(testResponseDto);

    // When
    GolfBallResponseDto result = golfBallService.createGolfBall(testCreateDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getBrand()).isEqualTo("Titleist");
    verify(golfBallRepository, times(1)).save(any(GolfBall.class));
  }

  @Test
  @DisplayName("Should get golf ball by ID")
  void testGetGolfBallById() {
    // Given
    when(golfBallRepository.findById(1L)).thenReturn(Optional.of(testGolfBall));
    when(golfBallMapper.toResponseDto(testGolfBall)).thenReturn(testResponseDto);

    // When
    GolfBallResponseDto result = golfBallService.getGolfBallById(1L);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(golfBallRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should throw exception when golf ball not found")
  void testGetGolfBallByIdNotFound() {
    // Given
    when(golfBallRepository.findById(999L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> golfBallService.getGolfBallById(999L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("GolfBall");
  }

  @Test
  @DisplayName("Should get all golf balls")
  void testGetAllGolfBalls() {
    // Given
    GolfBall ball2 = GolfBall.builder()
        .id(2L)
        .brand("Callaway")
        .model("Chrome Soft")
        .build();

    List<GolfBall> golfBalls = Arrays.asList(testGolfBall, ball2);
    when(golfBallRepository.findAll()).thenReturn(golfBalls);
    when(golfBallMapper.toResponseDto(any(GolfBall.class))).thenReturn(testResponseDto);

    // When
    List<GolfBallResponseDto> result = golfBallService.getAllGolfBalls();

    // Then
    assertThat(result).hasSize(2);
    verify(golfBallRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should get golf balls by brand")
  void testGetGolfBallsByBrand() {
    // Given
    List<GolfBall> golfBalls = Arrays.asList(testGolfBall);
    when(golfBallRepository.findByBrandIgnoreCase("Titleist")).thenReturn(golfBalls);
    when(golfBallMapper.toResponseDto(testGolfBall)).thenReturn(testResponseDto);

    // When
    List<GolfBallResponseDto> result = golfBallService.getGolfBallsByBrand("Titleist");

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getBrand()).isEqualTo("Titleist");
    verify(golfBallRepository, times(1)).findByBrandIgnoreCase("Titleist");
  }

  @Test
  @DisplayName("Should get golf balls by condition")
  void testGetGolfBallsByCondition() {
    // Given
    List<GolfBall> golfBalls = Arrays.asList(testGolfBall);
    when(golfBallRepository.findByCondition(BallCondition.MINT)).thenReturn(golfBalls);
    when(golfBallMapper.toResponseDto(testGolfBall)).thenReturn(testResponseDto);

    // When
    List<GolfBallResponseDto> result = golfBallService.getGolfBallsByCondition(BallCondition.MINT);

    // Then
    assertThat(result).hasSize(1);
    verify(golfBallRepository, times(1)).findByCondition(BallCondition.MINT);
  }

  @Test
  @DisplayName("Should get all brands")
  void testGetAllBrands() {
    // Given
    List<String> brands = Arrays.asList("Titleist", "Callaway", "TaylorMade");
    when(golfBallRepository.findAllBrands()).thenReturn(brands);

    // When
    List<String> result = golfBallService.getAllBrands();

    // Then
    assertThat(result).hasSize(3);
    assertThat(result).contains("Titleist", "Callaway", "TaylorMade");
    verify(golfBallRepository, times(1)).findAllBrands();
  }

  @Test
  @DisplayName("Should update golf ball")
  void testUpdateGolfBall() {
    // Given
    when(golfBallRepository.findById(1L)).thenReturn(Optional.of(testGolfBall));
    when(golfBallRepository.save(testGolfBall)).thenReturn(testGolfBall);
    when(golfBallMapper.toResponseDto(testGolfBall)).thenReturn(testResponseDto);
    doNothing().when(golfBallMapper).updateEntityFromDto(testCreateDto, testGolfBall);

    // When
    GolfBallResponseDto result = golfBallService.updateGolfBall(1L, testCreateDto);

    // Then
    assertThat(result).isNotNull();
    verify(golfBallRepository, times(1)).save(testGolfBall);
    verify(golfBallMapper, times(1)).updateEntityFromDto(testCreateDto, testGolfBall);
  }

  @Test
  @DisplayName("Should delete golf ball")
  void testDeleteGolfBall() {
    // Given
    when(golfBallRepository.findById(1L)).thenReturn(Optional.of(testGolfBall));
    doNothing().when(golfBallRepository).delete(testGolfBall);

    // When
    golfBallService.deleteGolfBall(1L);

    // Then
    verify(golfBallRepository, times(1)).delete(testGolfBall);
  }

  @Test
  @DisplayName("Should update stock successfully")
  void testUpdateStock() {
    // Given
    when(golfBallRepository.findById(1L)).thenReturn(Optional.of(testGolfBall));
    when(golfBallRepository.save(testGolfBall)).thenReturn(testGolfBall);

    // When
    golfBallService.updateStock(1L, 10);

    // Then
    assertThat(testGolfBall.getQuantity()).isEqualTo(110);
    verify(golfBallRepository, times(1)).save(testGolfBall);
  }

  @Test
  @DisplayName("Should throw exception when updating stock below zero")
  void testUpdateStockInsufficientStock() {
    // Given
    when(golfBallRepository.findById(1L)).thenReturn(Optional.of(testGolfBall));

    // When & Then
    assertThatThrownBy(() -> golfBallService.updateStock(1L, -150))
        .isInstanceOf(InsufficientStockException.class);
    verify(golfBallRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should get available golf balls")
  void testGetAvailableGolfBalls() {
    // Given
    List<GolfBall> availableBalls = Arrays.asList(testGolfBall);
    when(golfBallRepository.findAllAvailableOrderByPriceAsc()).thenReturn(availableBalls);
    when(golfBallMapper.toResponseDto(testGolfBall)).thenReturn(testResponseDto);

    // When
    List<GolfBallResponseDto> result = golfBallService.getAvailableGolfBalls();

    // Then
    assertThat(result).hasSize(1);
    verify(golfBallRepository, times(1)).findAllAvailableOrderByPriceAsc();
  }

  @Test
  @DisplayName("Should get golf balls by filters")
  void testGetGolfBallsByFilters() {
    // Given
    List<GolfBall> filteredBalls = Arrays.asList(testGolfBall);
    when(golfBallRepository.findByFilters(
        "Titleist",
        BallCondition.MINT,
        BigDecimal.valueOf(20.00),
        BigDecimal.valueOf(40.00))).thenReturn(filteredBalls);
    when(golfBallMapper.toResponseDto(testGolfBall)).thenReturn(testResponseDto);

    // When
    List<GolfBallResponseDto> result = golfBallService.getGolfBallsByFilters(
        "Titleist",
        BallCondition.MINT,
        BigDecimal.valueOf(20.00),
        BigDecimal.valueOf(40.00));

    // Then
    assertThat(result).hasSize(1);
    verify(golfBallRepository, times(1)).findByFilters(any(), any(), any(), any());
  }
}
