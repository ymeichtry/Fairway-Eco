package bbw.ch.FairwayEcoBackend.service;

import bbw.ch.FairwayEcoBackend.dto.CustomerCreateDto;
import bbw.ch.FairwayEcoBackend.dto.CustomerResponseDto;
import bbw.ch.FairwayEcoBackend.exception.DuplicateResourceException;
import bbw.ch.FairwayEcoBackend.exception.ResourceNotFoundException;
import bbw.ch.FairwayEcoBackend.mapper.CustomerMapper;
import bbw.ch.FairwayEcoBackend.model.Address;
import bbw.ch.FairwayEcoBackend.model.Customer;
import bbw.ch.FairwayEcoBackend.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Tests")
class CustomerServiceImplTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private CustomerMapper customerMapper;

  @InjectMocks
  private CustomerServiceImpl customerService;

  private Customer testCustomer;
  private CustomerCreateDto testCreateDto;
  private CustomerResponseDto testResponseDto;

  @BeforeEach
  void setUp() {
    Address address = Address.builder()
        .street("123 Golf Lane")
        .city("Golftown")
        .postalCode("12345")
        .country("USA")
        .build();

    testCustomer = Customer.builder()
        .id(1L)
        .firstName("John")
        .lastName("Doe")
        .email("john.doe@example.com")
        .phone("+1234567890")
        .address(address)
        .build();

    testCreateDto = new CustomerCreateDto();
    testCreateDto.setFirstName("John");
    testCreateDto.setLastName("Doe");
    testCreateDto.setEmail("john.doe@example.com");
    testCreateDto.setPhone("+1234567890");

    testResponseDto = new CustomerResponseDto();
    testResponseDto.setId(1L);
    testResponseDto.setFirstName("John");
    testResponseDto.setLastName("Doe");
    testResponseDto.setEmail("john.doe@example.com");
  }

  @Test
  @DisplayName("Should create customer successfully")
  void testCreateCustomer() {
    // Given
    when(customerRepository.existsByEmail(testCreateDto.getEmail())).thenReturn(false);
    when(customerMapper.toEntity(testCreateDto)).thenReturn(testCustomer);
    when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
    when(customerMapper.toResponseDto(testCustomer)).thenReturn(testResponseDto);

    // When
    CustomerResponseDto result = customerService.createCustomer(testCreateDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
    verify(customerRepository, times(1)).save(any(Customer.class));
  }

  @Test
  @DisplayName("Should throw exception when creating customer with duplicate email")
  void testCreateCustomerDuplicateEmail() {
    // Given
    when(customerRepository.existsByEmail(testCreateDto.getEmail())).thenReturn(true);

    // When & Then
    assertThatThrownBy(() -> customerService.createCustomer(testCreateDto))
        .isInstanceOf(DuplicateResourceException.class)
        .hasMessageContaining("email");
    verify(customerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should get customer by ID")
  void testGetCustomerById() {
    // Given
    when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
    when(customerMapper.toResponseDto(testCustomer)).thenReturn(testResponseDto);

    // When
    CustomerResponseDto result = customerService.getCustomerById(1L);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(1L);
    verify(customerRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should throw exception when customer not found by ID")
  void testGetCustomerByIdNotFound() {
    // Given
    when(customerRepository.findById(999L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> customerService.getCustomerById(999L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("Customer");
  }

  @Test
  @DisplayName("Should get customer by email")
  void testGetCustomerByEmail() {
    // Given
    when(customerRepository.findByEmail("john.doe@example.com"))
        .thenReturn(Optional.of(testCustomer));
    when(customerMapper.toResponseDto(testCustomer)).thenReturn(testResponseDto);

    // When
    CustomerResponseDto result = customerService.getCustomerByEmail("john.doe@example.com");

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
    verify(customerRepository, times(1)).findByEmail("john.doe@example.com");
  }

  @Test
  @DisplayName("Should throw exception when customer not found by email")
  void testGetCustomerByEmailNotFound() {
    // Given
    when(customerRepository.findByEmail("nonexistent@example.com"))
        .thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> customerService.getCustomerByEmail("nonexistent@example.com"))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessageContaining("email");
  }

  @Test
  @DisplayName("Should get all customers")
  void testGetAllCustomers() {
    // Given
    Customer customer2 = Customer.builder()
        .id(2L)
        .firstName("Jane")
        .lastName("Smith")
        .email("jane.smith@example.com")
        .build();

    List<Customer> customers = Arrays.asList(testCustomer, customer2);
    when(customerRepository.findAll()).thenReturn(customers);
    when(customerMapper.toResponseDto(any(Customer.class))).thenReturn(testResponseDto);

    // When
    List<CustomerResponseDto> result = customerService.getAllCustomers();

    // Then
    assertThat(result).hasSize(2);
    verify(customerRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should update customer successfully")
  void testUpdateCustomer() {
    // Given
    when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
    when(customerRepository.save(testCustomer)).thenReturn(testCustomer);
    when(customerMapper.toResponseDto(testCustomer)).thenReturn(testResponseDto);
    doNothing().when(customerMapper).updateEntityFromDto(testCreateDto, testCustomer);

    // When
    CustomerResponseDto result = customerService.updateCustomer(1L, testCreateDto);

    // Then
    assertThat(result).isNotNull();
    verify(customerRepository, times(1)).save(testCustomer);
    verify(customerMapper, times(1)).updateEntityFromDto(testCreateDto, testCustomer);
  }

  @Test
  @DisplayName("Should throw exception when updating customer with duplicate email")
  void testUpdateCustomerDuplicateEmail() {
    // Given
    testCustomer.setEmail("old@example.com");
    testCreateDto.setEmail("new@example.com");

    when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
    when(customerRepository.existsByEmail("new@example.com")).thenReturn(true);

    // When & Then
    assertThatThrownBy(() -> customerService.updateCustomer(1L, testCreateDto))
        .isInstanceOf(DuplicateResourceException.class)
        .hasMessageContaining("email");
    verify(customerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Should delete customer")
  void testDeleteCustomer() {
    // Given
    when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
    doNothing().when(customerRepository).delete(testCustomer);

    // When
    customerService.deleteCustomer(1L);

    // Then
    verify(customerRepository, times(1)).delete(testCustomer);
  }

  @Test
  @DisplayName("Should throw exception when deleting non-existent customer")
  void testDeleteCustomerNotFound() {
    // Given
    when(customerRepository.findById(999L)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> customerService.deleteCustomer(999L))
        .isInstanceOf(ResourceNotFoundException.class);
    verify(customerRepository, never()).delete(any());
  }
}
