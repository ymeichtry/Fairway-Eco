package bbw.ch.FairwayEcoBackend.repository;

import bbw.ch.FairwayEcoBackend.model.BallCondition;
import bbw.ch.FairwayEcoBackend.model.GolfBall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests for GolfBallRepository.
 */
@DataJpaTest
@DisplayName("GolfBall Repository Tests")
class GolfBallRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private GolfBallRepository golfBallRepository;

  private GolfBall titleistBall;
  private GolfBall callawayBall;

  @BeforeEach
  void setUp() {
    titleistBall = GolfBall.builder()
        .brand("Titleist")
        .model("Pro V1")
        .condition(BallCondition.MINT)
        .quantity(100)
        .price(BigDecimal.valueOf(29.99))
        .description("Premium golf ball")
        .build();

    callawayBall = GolfBall.builder()
        .brand("Callaway")
        .model("Chrome Soft")
        .condition(BallCondition.GRADE_A)
        .quantity(50)
        .price(BigDecimal.valueOf(24.99))
        .build();

    entityManager.persist(titleistBall);
    entityManager.persist(callawayBall);
    entityManager.flush();
  }

  @Test
  @DisplayName("Should find golf balls by brand (case insensitive)")
  void testFindByBrandIgnoreCase() {
    // When
    List<GolfBall> result = golfBallRepository.findByBrandIgnoreCase("titleist");

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getBrand()).isEqualTo("Titleist");
  }

  @Test
  @DisplayName("Should find golf balls by condition")
  void testFindByCondition() {
    // When
    List<GolfBall> result = golfBallRepository.findByCondition(BallCondition.MINT);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getCondition()).isEqualTo(BallCondition.MINT);
  }

  @Test
  @DisplayName("Should find all available golf balls ordered by price")
  void testFindAllAvailableOrderByPriceAsc() {
    // When
    List<GolfBall> result = golfBallRepository.findAllAvailableOrderByPriceAsc();

    // Then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getPrice()).isLessThan(result.get(1).getPrice());
  }

  @Test
  @DisplayName("Should find all unique brands")
  void testFindAllBrands() {
    // When
    List<String> result = golfBallRepository.findAllBrands();

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).contains("Titleist", "Callaway");
  }

  @Test
  @DisplayName("Should find golf balls by filters")
  void testFindByFilters() {
    // When
    List<GolfBall> result = golfBallRepository.findByFilters(
        "Titleist",
        BallCondition.MINT,
        BigDecimal.valueOf(20.00),
        BigDecimal.valueOf(35.00));

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getBrand()).isEqualTo("Titleist");
    assertThat(result.get(0).getCondition()).isEqualTo(BallCondition.MINT);
  }

  @Test
  @DisplayName("Should find golf balls by filters with null brand")
  void testFindByFiltersNullBrand() {
    // When
    List<GolfBall> result = golfBallRepository.findByFilters(
        null,
        BallCondition.MINT,
        null,
        null);

    // Then
    assertThat(result).hasSize(1);
  }

  @Test
  @DisplayName("Should save and retrieve golf ball")
  void testSaveAndRetrieve() {
    // Given
    GolfBall newBall = GolfBall.builder()
        .brand("TaylorMade")
        .model("TP5")
        .condition(BallCondition.GRADE_A)
        .price(new BigDecimal("5.00"))
        .quantity(25)
        .build();

    // When
    GolfBall saved = golfBallRepository.save(newBall);
    GolfBall retrieved = golfBallRepository.findById(saved.getId()).orElse(null);

    // Then
    assertThat(retrieved).isNotNull();
    assertThat(retrieved.getBrand()).isEqualTo("TaylorMade");
    assertThat(retrieved.getModel()).isEqualTo("TP5");
  }

  @Test
  @DisplayName("Should delete golf ball")
  void testDelete() {
    // Given
    Long ballId = titleistBall.getId();

    // When
    golfBallRepository.deleteById(ballId);
    entityManager.flush();

    // Then
    assertThat(golfBallRepository.findById(ballId)).isEmpty();
  }

  @Test
  @DisplayName("Should update golf ball quantity")
  void testUpdateQuantity() {
    // Given
    Long ballId = titleistBall.getId();

    // When
    titleistBall.setQuantity(150);
    golfBallRepository.save(titleistBall);
    entityManager.flush();
    entityManager.clear();

    // Then
    GolfBall updated = golfBallRepository.findById(ballId).orElse(null);
    assertThat(updated).isNotNull();
    assertThat(updated.getQuantity()).isEqualTo(150);
  }
}
