package bbw.ch.FairwayEcoBackend.controller;

import bbw.ch.FairwayEcoBackend.dto.CustomerCreateDto;
import bbw.ch.FairwayEcoBackend.dto.CustomerResponseDto;
import bbw.ch.FairwayEcoBackend.service.CustomerService;
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
 * Unit tests for CustomerController.
 */
@WebMvcTest(CustomerController.class)
@DisplayName("Customer Controller Tests")
class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CustomerService customerService;

  private CustomerResponseDto testResponseDto;
  private CustomerCreateDto testCreateDto;

  @BeforeEach
  void setUp() {
    testResponseDto = new CustomerResponseDto();
    testResponseDto.setId(1L);
    testResponseDto.setFirstName("John");
    testResponseDto.setLastName("Doe");
    testResponseDto.setEmail("john.doe@example.com");

    testCreateDto = new CustomerCreateDto();
    testCreateDto.setFirstName("John");
    testCreateDto.setLastName("Doe");
    testCreateDto.setEmail("john.doe@example.com");
    testCreateDto.setPhone("+1234567890");
  }

  @Test
  @DisplayName("POST /api/v1/customers - Should create customer")
  void testCreateCustomer() throws Exception {
    // Given
    when(customerService.createCustomer(any(CustomerCreateDto.class)))
        .thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(post("/api/v1/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testCreateDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.email").value("john.doe@example.com"));

    verify(customerService, times(1)).createCustomer(any(CustomerCreateDto.class));
  }

  @Test
  @DisplayName("GET /api/v1/customers/{id} - Should get customer by ID")
  void testGetCustomerById() throws Exception {
    // Given
    when(customerService.getCustomerById(1L)).thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(get("/api/v1/customers/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.email").value("john.doe@example.com"));

    verify(customerService, times(1)).getCustomerById(1L);
  }

  @Test
  @DisplayName("GET /api/v1/customers/email/{email} - Should get customer by email")
  void testGetCustomerByEmail() throws Exception {
    // Given
    when(customerService.getCustomerByEmail("john.doe@example.com"))
        .thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(get("/api/v1/customers/email/{email}", "john.doe@example.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("john.doe@example.com"));

    verify(customerService, times(1)).getCustomerByEmail("john.doe@example.com");
  }

  @Test
  @DisplayName("GET /api/v1/customers - Should get all customers")
  void testGetAllCustomers() throws Exception {
    // Given
    CustomerResponseDto customer2 = new CustomerResponseDto();
    customer2.setId(2L);
    customer2.setEmail("jane.smith@example.com");

    List<CustomerResponseDto> customers = Arrays.asList(testResponseDto, customer2);
    when(customerService.getAllCustomers()).thenReturn(customers);

    // When & Then
    mockMvc.perform(get("/api/v1/customers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
        .andExpect(jsonPath("$[1].email").value("jane.smith@example.com"));

    verify(customerService, times(1)).getAllCustomers();
  }

  @Test
  @DisplayName("PUT /api/v1/customers/{id} - Should update customer")
  void testUpdateCustomer() throws Exception {
    // Given
    when(customerService.updateCustomer(eq(1L), any(CustomerCreateDto.class)))
        .thenReturn(testResponseDto);

    // When & Then
    mockMvc.perform(put("/api/v1/customers/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testCreateDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));

    verify(customerService, times(1)).updateCustomer(eq(1L), any(CustomerCreateDto.class));
  }

  @Test
  @DisplayName("DELETE /api/v1/customers/{id} - Should delete customer")
  void testDeleteCustomer() throws Exception {
    // Given
    doNothing().when(customerService).deleteCustomer(1L);

    // When & Then
    mockMvc.perform(delete("/api/v1/customers/{id}", 1L))
        .andExpect(status().isNoContent());

    verify(customerService, times(1)).deleteCustomer(1L);
  }

  @Test
  @DisplayName("POST /api/v1/customers - Should return 400 for invalid email")
  void testCreateCustomerInvalidEmail() throws Exception {
    // Given
    testCreateDto.setEmail("invalid-email");

    // When & Then
    mockMvc.perform(post("/api/v1/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testCreateDto)))
        .andExpect(status().isBadRequest());

    verify(customerService, never()).createCustomer(any());
  }

  @Test
  @DisplayName("POST /api/v1/customers - Should return 400 for missing required fields")
  void testCreateCustomerMissingFields() throws Exception {
    // Given
    CustomerCreateDto invalidDto = new CustomerCreateDto();
    // Missing required fields

    // When & Then
    mockMvc.perform(post("/api/v1/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest());

    verify(customerService, never()).createCustomer(any());
  }
}
