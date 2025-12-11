package bbw.ch.FairwayEcoBackend.service;

import bbw.ch.FairwayEcoBackend.dto.CustomerCreateDto;
import bbw.ch.FairwayEcoBackend.dto.CustomerResponseDto;

import java.util.List;

/**
 * Service interface for Customer operations.
 */
public interface CustomerService {

  CustomerResponseDto createCustomer(CustomerCreateDto dto);

  CustomerResponseDto getCustomerById(Long id);

  CustomerResponseDto getCustomerByEmail(String email);

  List<CustomerResponseDto> getAllCustomers();

  CustomerResponseDto updateCustomer(Long id, CustomerCreateDto dto);

  void deleteCustomer(Long id);
}
