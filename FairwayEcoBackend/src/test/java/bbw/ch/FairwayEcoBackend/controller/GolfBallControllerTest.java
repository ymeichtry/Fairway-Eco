package bbw.ch.FairwayEcoBackend.controller;

import bbw.ch.FairwayEcoBackend.dto.GolfBallCreateDto;
import bbw.ch.FairwayEcoBackend.dto.GolfBallResponseDto;
import bbw.ch.FairwayEcoBackend.model.BallCondition;
import bbw.ch.FairwayEcoBackend.service.GolfBallService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for GolfBallController.
 */
@WebMvcTest(GolfBallController.class)
@DisplayName("GolfBall Controller Tests")
class GolfBallControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private GolfBallService golfBallService;

  private GolfBallResponseDto testResponseDto;
  private GolfBallCreateDto testCreateDto;

  @BeforeEach
  void setUp() {
    testResponseDto = new GolfBallResponseDto();
    testResponseDto.setId(1L);
    testResponseDto.setBrand("Titleist");
    testResponseDto.setModel("Pro V1");
    testResponseDto.setCondition(BallCondition.MINT);
    testResponseDto.setQuantity(100);
    testResponseDto.setPrice(BigDecimal.valueOf(29.99));
    testResponseDto.setDescription("Premium golf ball");

    testCreateDto = new GolfBallCreateDto();
    testCreateDto.setBrand("Titleist");
    testCreateDto.setModel("Pro V1");
    testCreateDto.setCondition(BallCondition.MINT);
    testCreateDto.setQuantity(100);
    testCreateDto.setPrice(BigDecimal.valueOf(29.99));
    testCreateDto.setDescription("Premium golf ball");
  }

  @Test
  @DisplayName("POST /api/v1/golf-balls - Should create golf ball")
  void testCreateGolfBall() throws Exception {
    // Given
    when(golfBallService.createGolfBall(any(GolfBallCreateDto.class)))
        .thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(post("/api/v1/golf-balls")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testCreateDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.brand").value("Titleist"))
        .andExpect(jsonPath("$.model").value("Pro V1"));

    verify(golfBallService, times(1)).createGolfBall(any(GolfBallCreateDto.class));
  }

  @Test
  @DisplayName("GET /api/v1/golf-balls/{id} - Should get golf ball by ID")
  void testGetGolfBallById() throws Exception {
    // Given
    when(golfBallService.getGolfBallById(1L)).thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(get("/api/v1/golf-balls/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.brand").value("Titleist"));

    verify(golfBallService, times(1)).getGolfBallById(1L);
  }

  @Test
  @DisplayName("GET /api/v1/golf-balls - Should get all golf balls")
  void testGetAllGolfBalls() throws Exception {
    // Given
    GolfBallResponseDto ball2 = new GolfBallResponseDto();
    ball2.setId(2L);
    ball2.setBrand("Callaway");

    List<GolfBallResponseDto> balls = Arrays.asList(testResponseDto, ball2);
    when(golfBallService.getAllGolfBalls()).thenReturn(balls);

    // When & Then
    mockMvc.perform(get("/api/v1/golf-balls"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].brand").value("Titleist"))
        .andExpect(jsonPath("$[1].brand").value("Callaway"));

    verify(golfBallService, times(1)).getAllGolfBalls();
  }

  @Test
  @DisplayName("GET /api/v1/golf-balls/available - Should get available golf balls")
  void testGetAvailableGolfBalls() throws Exception {
    // Given
    List<GolfBallResponseDto> balls = Arrays.asList(testResponseDto);
    when(golfBallService.getAvailableGolfBalls()).thenReturn(balls);

    // When & Then
    mockMvc.perform(get("/api/v1/golf-balls/available"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));

    verify(golfBallService, times(1)).getAvailableGolfBalls();
  }

  @Test
  @DisplayName("GET /api/v1/golf-balls/brands - Should get all brands")
  void testGetAllBrands() throws Exception {
    // Given
    List<String> brands = Arrays.asList("Titleist", "Callaway", "TaylorMade");
    when(golfBallService.getAllBrands()).thenReturn(brands);

    // When & Then
    mockMvc.perform(get("/api/v1/golf-balls/brands"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0]").value("Titleist"));

    verify(golfBallService, times(1)).getAllBrands();
  }

  @Test
  @DisplayName("GET /api/v1/golf-balls/brand/{brand} - Should get golf balls by brand")
  void testGetGolfBallsByBrand() throws Exception {
    // Given
    List<GolfBallResponseDto> balls = Arrays.asList(testResponseDto);
    when(golfBallService.getGolfBallsByBrand("Titleist")).thenReturn(balls);

    // When & Then
    mockMvc.perform(get("/api/v1/golf-balls/brand/{brand}", "Titleist"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].brand").value("Titleist"));

    verify(golfBallService, times(1)).getGolfBallsByBrand("Titleist");
  }

  @Test
  @DisplayName("GET /api/v1/golf-balls/condition/{condition} - Should get golf balls by condition")
  void testGetGolfBallsByCondition() throws Exception {
    // Given
    List<GolfBallResponseDto> balls = Arrays.asList(testResponseDto);
    when(golfBallService.getGolfBallsByCondition(BallCondition.MINT)).thenReturn(balls);

    // When & Then
    mockMvc.perform(get("/api/v1/golf-balls/condition/{condition}", "MINT"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));

    verify(golfBallService, times(1)).getGolfBallsByCondition(BallCondition.MINT);
  }

  @Test
  @DisplayName("PUT /api/v1/golf-balls/{id} - Should update golf ball")
  void testUpdateGolfBall() throws Exception {
    // Given
    when(golfBallService.updateGolfBall(eq(1L), any(GolfBallCreateDto.class)))
        .thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(put("/api/v1/golf-balls/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testCreateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));

    verify(golfBallService, times(1)).updateGolfBall(eq(1L), any(GolfBallCreateDto.class));
  }

  @Test
  @DisplayName("DELETE /api/v1/golf-balls/{id} - Should delete golf ball")
  void testDeleteGolfBall() throws Exception {
    // Given
    doNothing().when(golfBallService).deleteGolfBall(1L);

    // When & Then
    mockMvc.perform(delete("/api/v1/golf-balls/{id}", 1L))
        .andExpect(status().isNoContent());

    verify(golfBallService, times(1)).deleteGolfBall(1L);
  }

  @Test
  @DisplayName("POST /api/v1/golf-balls - Should return 400 for invalid data")
  void testCreateGolfBallInvalidData() throws Exception {
    // Given
    GolfBallCreateDto invalidDto = new GolfBallCreateDto();
    // Missing required fields

    // When & Then
    mockMvc.perform(post("/api/v1/golf-balls")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest());

    verify(golfBallService, never()).createGolfBall(any());
  }
}
