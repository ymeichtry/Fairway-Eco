package bbw.ch.FairwayEcoBackend.service;

import bbw.ch.FairwayEcoBackend.dto.CustomerCreateDto;
import bbw.ch.FairwayEcoBackend.dto.CustomerResponseDto;
import bbw.ch.FairwayEcoBackend.exception.DuplicateResourceException;
import bbw.ch.FairwayEcoBackend.exception.ResourceNotFoundException;
import bbw.ch.FairwayEcoBackend.mapper.CustomerMapper;
import bbw.ch.FairwayEcoBackend.model.Customer;
import bbw.ch.FairwayEcoBackend.repository.CustomerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CustomerService.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Override
  @CircuitBreaker(name = "customerService", fallbackMethod = "createCustomerFallback")
  public CustomerResponseDto createCustomer(CustomerCreateDto dto) {
    log.info("Creating new customer with email: {}", dto.getEmail());

    if (customerRepository.existsByEmail(dto.getEmail())) {
      throw new DuplicateResourceException("Customer", "email", dto.getEmail());
    }

    Customer customer = customerMapper.toEntity(dto);
    Customer saved = customerRepository.save(customer);
    log.info("Created customer with ID: {}", saved.getId());
    return customerMapper.toResponseDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  @CircuitBreaker(name = "customerService", fallbackMethod = "getCustomerByIdFallback")
  public CustomerResponseDto getCustomerById(Long id) {
    Customer customer = findCustomerById(id);
    return customerMapper.toResponseDto(customer);
  }

  @Override
  @Transactional(readOnly = true)
  @CircuitBreaker(name = "customerService", fallbackMethod = "getCustomerByEmailFallback")
  public CustomerResponseDto getCustomerByEmail(String email) {
    Customer customer = customerRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
    return customerMapper.toResponseDto(customer);
  }

  @Override
  @Transactional(readOnly = true)
  @CircuitBreaker(name = "customerService", fallbackMethod = "getAllCustomersFallback")
  public List<CustomerResponseDto> getAllCustomers() {
    return customerRepository.findAll().stream()
        .map(customerMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @CircuitBreaker(name = "customerService", fallbackMethod = "updateCustomerFallback")
  public CustomerResponseDto updateCustomer(Long id, CustomerCreateDto dto) {
    log.info("Updating customer with ID: {}", id);
    Customer customer = findCustomerById(id);

    // Check if email is being changed and if new email already exists
    if (!customer.getEmail().equals(dto.getEmail()) &&
        customerRepository.existsByEmail(dto.getEmail())) {
      throw new DuplicateResourceException("Customer", "email", dto.getEmail());
    }

    customerMapper.updateEntityFromDto(dto, customer);
    Customer saved = customerRepository.save(customer);
    log.info("Updated customer with ID: {}", saved.getId());
    return customerMapper.toResponseDto(saved);
  }

  @Override
  @CircuitBreaker(name = "customerService", fallbackMethod = "deleteCustomerFallback")
  public void deleteCustomer(Long id) {
    log.info("Deleting customer with ID: {}", id);
    Customer customer = findCustomerById(id);
    customerRepository.delete(customer);
    log.info("Deleted customer with ID: {}", id);
  }

  private Customer findCustomerById(Long id) {
    return customerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
  }

  // Fallback methods
  private CustomerResponseDto createCustomerFallback(CustomerCreateDto dto, Exception ex) {
    log.error("Circuit breaker fallback: Failed to create customer", ex);
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
  }

  private CustomerResponseDto getCustomerByIdFallback(Long id, Exception ex) {
    log.error("Circuit breaker fallback: Failed to get customer with ID: {}", id, ex);
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
  }

  private CustomerResponseDto getCustomerByEmailFallback(String email, Exception ex) {
    log.error("Circuit breaker fallback: Failed to get customer with email: {}", email, ex);
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
  }

  private List<CustomerResponseDto> getAllCustomersFallback(Exception ex) {
    log.error("Circuit breaker fallback: Failed to get all customers", ex);
    return new ArrayList<>();
  }

  private CustomerResponseDto updateCustomerFallback(Long id, CustomerCreateDto dto, Exception ex) {
    log.error("Circuit breaker fallback: Failed to update customer with ID: {}", id, ex);
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
  }

  private void deleteCustomerFallback(Long id, Exception ex) {
    log.error("Circuit breaker fallback: Failed to delete customer with ID: {}", id, ex);
    throw new RuntimeException("Service temporarily unavailable. Please try again later.");
  }
}
