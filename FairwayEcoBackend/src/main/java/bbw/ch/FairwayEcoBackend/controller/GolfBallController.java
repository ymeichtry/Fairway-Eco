package bbw.ch.FairwayEcoBackend.controller;

import bbw.ch.FairwayEcoBackend.dto.GolfBallCreateDto;
import bbw.ch.FairwayEcoBackend.dto.GolfBallResponseDto;
import bbw.ch.FairwayEcoBackend.model.BallCondition;
import bbw.ch.FairwayEcoBackend.service.GolfBallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for GolfBall operations.
 */
@RestController
@RequestMapping("/api/v1/golf-balls")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GolfBallController {

  private final GolfBallService golfBallService;

  @PostMapping
  public ResponseEntity<GolfBallResponseDto> createGolfBall(
      @Valid @RequestBody GolfBallCreateDto dto) {
    GolfBallResponseDto created = golfBallService.createGolfBall(dto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GolfBallResponseDto> getGolfBallById(@PathVariable Long id) {
    return ResponseEntity.ok(golfBallService.getGolfBallById(id));
  }

  @GetMapping
  public ResponseEntity<List<GolfBallResponseDto>> getAllGolfBalls() {
    return ResponseEntity.ok(golfBallService.getAllGolfBalls());
  }

  @GetMapping("/available")
  public ResponseEntity<List<GolfBallResponseDto>> getAvailableGolfBalls() {
    return ResponseEntity.ok(golfBallService.getAvailableGolfBalls());
  }

  @GetMapping("/brand/{brand}")
  public ResponseEntity<List<GolfBallResponseDto>> getGolfBallsByBrand(
      @PathVariable String brand) {
    return ResponseEntity.ok(golfBallService.getGolfBallsByBrand(brand));
  }

  @GetMapping("/condition/{condition}")
  public ResponseEntity<List<GolfBallResponseDto>> getGolfBallsByCondition(
      @PathVariable BallCondition condition) {
    return ResponseEntity.ok(golfBallService.getGolfBallsByCondition(condition));
  }

  @GetMapping("/filter")
  public ResponseEntity<List<GolfBallResponseDto>> getGolfBallsByFilters(
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) BallCondition condition,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice) {
    return ResponseEntity.ok(golfBallService.getGolfBallsByFilters(brand, condition, minPrice, maxPrice));
  }

  @GetMapping("/brands")
  public ResponseEntity<List<String>> getAllBrands() {
    return ResponseEntity.ok(golfBallService.getAllBrands());
  }

  @PutMapping("/{id}")
  public ResponseEntity<GolfBallResponseDto> updateGolfBall(
      @PathVariable Long id,
      @Valid @RequestBody GolfBallCreateDto dto) {
    return ResponseEntity.ok(golfBallService.updateGolfBall(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteGolfBall(@PathVariable Long id) {
    golfBallService.deleteGolfBall(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/stock")
  public ResponseEntity<Void> updateStock(
      @PathVariable Long id,
      @RequestParam Integer quantityChange) {
    golfBallService.updateStock(id, quantityChange);
    return ResponseEntity.ok().build();
  }
}
