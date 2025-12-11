package bbw.ch.FairwayEcoBackend.controller;

import bbw.ch.FairwayEcoBackend.dto.CustomerCreateDto;
import bbw.ch.FairwayEcoBackend.dto.CustomerResponseDto;
import bbw.ch.FairwayEcoBackend.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Customer operations.
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  public ResponseEntity<CustomerResponseDto> createCustomer(
      @Valid @RequestBody CustomerCreateDto dto) {
    CustomerResponseDto created = customerService.createCustomer(dto);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
    return ResponseEntity.ok(customerService.getCustomerById(id));
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<CustomerResponseDto> getCustomerByEmail(@PathVariable String email) {
    return ResponseEntity.ok(customerService.getCustomerByEmail(email));
  }

  @GetMapping
  public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
    return ResponseEntity.ok(customerService.getAllCustomers());
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerResponseDto> updateCustomer(
      @PathVariable Long id,
      @Valid @RequestBody CustomerCreateDto dto) {
    return ResponseEntity.ok(customerService.updateCustomer(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    customerService.deleteCustomer(id);
    return ResponseEntity.noContent().build();
  }
}
