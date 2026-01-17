package bbw.ch.FairwayEcoBackend.repository;

import bbw.ch.FairwayEcoBackend.model.Address;
import bbw.ch.FairwayEcoBackend.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests for CustomerRepository.
 */
@DataJpaTest
@DisplayName("Customer Repository Tests")
class CustomerRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CustomerRepository customerRepository;

  private Customer testCustomer;

  @BeforeEach
  void setUp() {
    Address address = Address.builder()
        .street("123 Golf Lane")
        .city("Golftown")
        .postalCode("12345")
        .country("USA")
        .build();

    testCustomer = Customer.builder()
        .firstName("John")
        .lastName("Doe")
        .email("john.doe@example.com")
        .phone("+1234567890")
        .address(address)
        .build();

    entityManager.persist(testCustomer);
  }

  @Test
  @DisplayName("Should return empty when customer email not found")
  void testFindByEmailNotFound() {
    // When
    Optional<Customer> result = customerRepository.findByEmail("nonexistent@example.com");

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Should check if customer exists by email")
  void testExistsByEmail() {
    // When
    boolean exists = customerRepository.existsByEmail("john.doe@example.com");
    boolean notExists = customerRepository.existsByEmail("nonexistent@example.com");

    // Then
    assertThat(exists).isTrue();
    assertThat(notExists).isFalse();
  }

  @Test
  @DisplayName("Should save and retrieve customer with address")
  void testSaveAndRetrieveWithAddress() {
    // Given
    Address newAddress = Address.builder()
        .street("456 Fairway Dr")
        .city("Golfville")
        .postalCode("54321")
        .country("USA")
        .build();

    Customer newCustomer = Customer.builder()
        .firstName("Jane")
        .lastName("Smith")
        .email("jane.smith@example.com")
        .phone("+0987654321")
        .address(newAddress)
        .build();

    // When
    Customer saved = customerRepository.save(newCustomer);
    Customer retrieved = customerRepository.findById(saved.getId()).orElse(null);

    // Then
    assertThat(retrieved).isNotNull();
    assertThat(retrieved.getEmail()).isEqualTo("jane.smith@example.com");
    assertThat(retrieved.getAddress()).isNotNull();
    assertThat(retrieved.getAddress().getStreet()).isEqualTo("456 Fairway Dr");
  }

  @Test
  @DisplayName("Should update customer details")
  void testUpdateCustomer() {
    // Given
    Long customerId = testCustomer.getId();

    // When
    testCustomer.setPhone("+9999999999");
    customerRepository.save(testCustomer);
    entityManager.flush();
    entityManager.clear();

    // Then
    Customer updated = customerRepository.findById(customerId).orElse(null);
    assertThat(updated).isNotNull();
    assertThat(updated.getPhone()).isEqualTo("+9999999999");
  }

  @Test
  @DisplayName("Should delete customer")
  void testDelete() {
    // Given
    Long customerId = testCustomer.getId();

    // When
    customerRepository.deleteById(customerId);
    entityManager.flush();

    // Then
    assertThat(customerRepository.findById(customerId)).isEmpty();
  }

  @Test
  @DisplayName("Should maintain email uniqueness")
  void testEmailUniqueness() {
    // Given
    Address address = Address.builder()
        .street("789 Green Ave")
        .city("Puttington")
        .postalCode("99999")
        .country("USA")
        .build();

    Customer duplicateEmail = Customer.builder()
        .firstName("Bob")
        .lastName("Johnson")
        .email("john.doe@example.com") // Duplicate email
        .phone("+1111111111")
        .address(address)
        .build();

    // When/Then
    try {
      customerRepository.save(duplicateEmail);
      entityManager.flush();
      assertThat(false).as("Should have thrown exception for duplicate email").isTrue();
    } catch (Exception e) {
      // Expected - duplicate email should cause constraint violation
      assertThat(e).isNotNull();
    }
  }
}
