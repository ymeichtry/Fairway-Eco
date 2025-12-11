package bbw.ch.FairwayEcoBackend.service;

import bbw.ch.FairwayEcoBackend.dto.CustomerCreateDto;
import bbw.ch.FairwayEcoBackend.dto.CustomerResponseDto;
import bbw.ch.FairwayEcoBackend.exception.DuplicateResourceException;
import bbw.ch.FairwayEcoBackend.exception.ResourceNotFoundException;
import bbw.ch.FairwayEcoBackend.mapper.CustomerMapper;
import bbw.ch.FairwayEcoBackend.model.Customer;
import bbw.ch.FairwayEcoBackend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public CustomerResponseDto getCustomerById(Long id) {
    Customer customer = findCustomerById(id);
    return customerMapper.toResponseDto(customer);
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerResponseDto getCustomerByEmail(String email) {
    Customer customer = customerRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Customer", "email", email));
    return customerMapper.toResponseDto(customer);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CustomerResponseDto> getAllCustomers() {
    return customerRepository.findAll().stream()
        .map(customerMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
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
}
