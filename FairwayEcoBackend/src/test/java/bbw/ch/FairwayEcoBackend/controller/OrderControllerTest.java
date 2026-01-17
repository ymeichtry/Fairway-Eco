package bbw.ch.FairwayEcoBackend.controller;

import bbw.ch.FairwayEcoBackend.dto.OrderCreateDto;
import bbw.ch.FairwayEcoBackend.dto.OrderItemDto;
import bbw.ch.FairwayEcoBackend.dto.OrderResponseDto;
import bbw.ch.FairwayEcoBackend.dto.OrderStatusUpdateDto;
import bbw.ch.FairwayEcoBackend.model.OrderStatus;
import bbw.ch.FairwayEcoBackend.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for OrderController.
 */
@WebMvcTest(OrderController.class)
@DisplayName("Order Controller Tests")
class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrderService orderService;

  private OrderResponseDto testResponseDto;
  private OrderCreateDto testCreateDto;

  @BeforeEach
  void setUp() {
    testResponseDto = new OrderResponseDto();
    testResponseDto.setId(1L);
    testResponseDto.setStatus(OrderStatus.PENDING);

    OrderItemDto itemDto = new OrderItemDto();
    itemDto.setGolfBallId(1L);
    itemDto.setQuantity(10);

    testCreateDto = new OrderCreateDto();
    testCreateDto.setCustomerId(1L);
    testCreateDto.setItems(Arrays.asList(itemDto));
  }

  @Test
  @DisplayName("POST /api/v1/orders - Should create order")
  void testCreateOrder() throws Exception {
    // Given
    when(orderService.createOrder(any(OrderCreateDto.class)))
        .thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(post("/api/v1/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testCreateDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.status").value("PENDING"));

    verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
  }

  @Test
  @DisplayName("GET /api/v1/orders/{id} - Should get order by ID")
  void testGetOrderById() throws Exception {
    // Given
    when(orderService.getOrderById(1L)).thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(get("/api/v1/orders/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.status").value("PENDING"));

    verify(orderService, times(1)).getOrderById(1L);
  }

  @Test
  @DisplayName("GET /api/v1/orders - Should get all orders")
  void testGetAllOrders() throws Exception {
    // Given
    OrderResponseDto order2 = new OrderResponseDto();
    order2.setId(2L);
    order2.setStatus(OrderStatus.PAID);

    List<OrderResponseDto> orders = Arrays.asList(testResponseDto, order2);
    when(orderService.getAllOrders()).thenReturn(orders);

    // When & Then
    mockMvc.perform(get("/api/v1/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].status").value("PENDING"))
        .andExpect(jsonPath("$[1].status").value("PAID"));

    verify(orderService, times(1)).getAllOrders();
  }

  @Test
  @DisplayName("GET /api/v1/orders/customer/{customerId} - Should get orders by customer ID")
  void testGetOrdersByCustomerId() throws Exception {
    // Given
    List<OrderResponseDto> orders = Arrays.asList(testResponseDto);
    when(orderService.getOrdersByCustomerId(1L)).thenReturn(orders);

    // When & Then
    mockMvc.perform(get("/api/v1/orders/customer/{customerId}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(1));

    verify(orderService, times(1)).getOrdersByCustomerId(1L);
  }

  @Test
  @DisplayName("GET /api/v1/orders/status/{status} - Should get orders by status")
  void testGetOrdersByStatus() throws Exception {
    // Given
    List<OrderResponseDto> orders = Arrays.asList(testResponseDto);
    when(orderService.getOrdersByStatus(OrderStatus.PENDING)).thenReturn(orders);

    // When & Then
    mockMvc.perform(get("/api/v1/orders/status/{status}", "PENDING"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].status").value("PENDING"));

    verify(orderService, times(1)).getOrdersByStatus(OrderStatus.PENDING);
  }

  @Test
  @DisplayName("PATCH /api/v1/orders/{id}/status - Should update order status")
  void testUpdateOrderStatus() throws Exception {
    // Given
    OrderStatusUpdateDto statusUpdate = new OrderStatusUpdateDto();
    statusUpdate.setStatus(OrderStatus.PAID);

    testResponseDto.setStatus(OrderStatus.PAID);
    when(orderService.updateOrderStatus(eq(1L), eq(OrderStatus.PAID)))
        .thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(patch("/api/v1/orders/{id}/status", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(statusUpdate)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("PAID"));

    verify(orderService, times(1)).updateOrderStatus(1L, OrderStatus.PAID);
  }

  @Test
  @DisplayName("POST /api/v1/orders/{id}/cancel - Should cancel order")
  void testCancelOrder() throws Exception {
    // Given
    doNothing().when(orderService).cancelOrder(1L);

    // When & Then
    mockMvc.perform(post("/api/v1/orders/{id}/cancel", 1L))
        .andExpect(status().isOk());

    verify(orderService, times(1)).cancelOrder(1L);
  }

  @Test
  @DisplayName("POST /api/v1/orders - Should return 400 for invalid data")
  void testCreateOrderInvalidData() throws Exception {
    // Given
    OrderCreateDto invalidDto = new OrderCreateDto();
    // Missing required fields

    // When & Then
    mockMvc.perform(post("/api/v1/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest());

    verify(orderService, never()).createOrder(any());
  }

  @Test
  @DisplayName("POST /api/v1/orders - Should return 400 for empty items list")
  void testCreateOrderEmptyItems() throws Exception {
    // Given
    testCreateDto.setItems(Arrays.asList());

    // When & Then
    mockMvc.perform(post("/api/v1/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testCreateDto)))
        .andExpect(status().isBadRequest());

    verify(orderService, never()).createOrder(any());
  }
}
