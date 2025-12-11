package bbw.ch.FairwayEcoBackend.repository;

import bbw.ch.FairwayEcoBackend.model.BallCondition;
import bbw.ch.FairwayEcoBackend.model.GolfBall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for GolfBall entity operations.
 */
@Repository
public interface GolfBallRepository extends JpaRepository<GolfBall, Long> {

  List<GolfBall> findByBrandIgnoreCase(String brand);

  List<GolfBall> findByCondition(BallCondition condition);

  List<GolfBall> findByBrandIgnoreCaseAndCondition(String brand, BallCondition condition);

  List<GolfBall> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

  List<GolfBall> findByQuantityGreaterThan(Integer quantity);

  @Query("SELECT g FROM GolfBall g WHERE g.quantity > 0 ORDER BY g.price ASC")
  List<GolfBall> findAllAvailableOrderByPriceAsc();

  @Query("SELECT DISTINCT g.brand FROM GolfBall g ORDER BY g.brand")
  List<String> findAllBrands();

  @Query("SELECT g FROM GolfBall g WHERE " +
      "(:brand IS NULL OR LOWER(g.brand) = LOWER(:brand)) AND " +
      "(:condition IS NULL OR g.condition = :condition) AND " +
      "(:minPrice IS NULL OR g.price >= :minPrice) AND " +
      "(:maxPrice IS NULL OR g.price <= :maxPrice)")
  List<GolfBall> findByFilters(
      @Param("brand") String brand,
      @Param("condition") BallCondition condition,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice);
}
