package bbw.ch.FairwayEcoBackend.repository;

import bbw.ch.FairwayEcoBackend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests for OrderRepository.
 */
@DataJpaTest
@DisplayName("Order Repository Tests")
class OrderRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private OrderRepository orderRepository;

  private Customer testCustomer;
  private GolfBall testGolfBall;
  private Order testOrder;

  @BeforeEach
  void setUp() {
    // Create customer
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

    // Create golf ball
    testGolfBall = GolfBall.builder()
        .brand("Titleist")
        .model("Pro V1")
        .condition(BallCondition.MINT)
        .quantity(100)
        .price(BigDecimal.valueOf(29.99))
        .build();
    entityManager.persist(testGolfBall);

    // Create order
    testOrder = Order.builder()
        .customer(testCustomer)
        .status(OrderStatus.PENDING)
        .shippingAddress(address)
        .build();

    OrderItem orderItem = OrderItem.builder()
        .golfBall(testGolfBall)
        .quantity(10)
        .unitPrice(BigDecimal.valueOf(29.99))
        .build();
    testOrder.addItem(orderItem);

    entityManager.persist(testOrder);
    entityManager.flush();
  }

  @Test
  @DisplayName("Should find orders by customer ID")
  void testFindByCustomerId() {
    // When
    List<Order> result = orderRepository.findByCustomerId(testCustomer.getId());

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getCustomer().getId()).isEqualTo(testCustomer.getId());
  }

  @Test
  @DisplayName("Should find orders by status")
  void testFindByStatus() {
    // When
    List<Order> result = orderRepository.findByStatus(OrderStatus.PENDING);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
  }

  @Test
  @DisplayName("Should save order with items")
  void testSaveOrderWithItems() {
    // Given
    Order newOrder = Order.builder()
        .customer(testCustomer)
        .status(OrderStatus.PAID)
        .shippingAddress(testCustomer.getAddress())
        .build();

    OrderItem item = OrderItem.builder()
        .golfBall(testGolfBall)
        .quantity(5)
        .unitPrice(BigDecimal.valueOf(29.99))
        .build();
    newOrder.addItem(item);

    // When
    Order saved = orderRepository.save(newOrder);
    Order retrieved = orderRepository.findById(saved.getId()).orElse(null);

    // Then
    assertThat(retrieved).isNotNull();
    assertThat(retrieved.getItems()).hasSize(1);
    assertThat(retrieved.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(149.95));
  }

  @Test
  @DisplayName("Should calculate total amount correctly")
  void testTotalAmountCalculation() {
    // When
    Order retrieved = orderRepository.findById(testOrder.getId()).orElse(null);

    // Then
    assertThat(retrieved).isNotNull();
    assertThat(retrieved.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(299.90));
  }

  @Test
  @DisplayName("Should update order status")
  void testUpdateOrderStatus() {
    // Given
    Long orderId = testOrder.getId();

    // When
    testOrder.setStatus(OrderStatus.SHIPPED);
    orderRepository.save(testOrder);
    entityManager.flush();
    entityManager.clear();

    // Then
    Order updated = orderRepository.findById(orderId).orElse(null);
    assertThat(updated).isNotNull();
    assertThat(updated.getStatus()).isEqualTo(OrderStatus.SHIPPED);
  }

  @Test
  @DisplayName("Should maintain order-item relationship")
  void testOrderItemRelationship() {
    // When
    Order retrieved = orderRepository.findById(testOrder.getId()).orElse(null);

    // Then
    assertThat(retrieved).isNotNull();
    assertThat(retrieved.getItems()).isNotEmpty();
    assertThat(retrieved.getItems().get(0).getOrder()).isEqualTo(retrieved);
  }

  @Test
  @DisplayName("Should delete order cascade to items")
  void testCascadeDelete() {
    // Given
    Long orderId = testOrder.getId();

    // When
    orderRepository.deleteById(orderId);
    entityManager.flush();

    // Then
    assertThat(orderRepository.findById(orderId)).isEmpty();
  }

  @Test
  @DisplayName("Should find multiple orders for same customer")
  void testMultipleOrdersPerCustomer() {
    // Given
    Order secondOrder = Order.builder()
        .customer(testCustomer)
        .status(OrderStatus.PROCESSING)
        .shippingAddress(testCustomer.getAddress())
        .build();

    OrderItem item = OrderItem.builder()
        .golfBall(testGolfBall)
        .quantity(3)
        .unitPrice(BigDecimal.valueOf(29.99))
        .build();
    secondOrder.addItem(item);

    entityManager.persist(secondOrder);
    entityManager.flush();

    // When
    List<Order> result = orderRepository.findByCustomerId(testCustomer.getId());

    // Then
    assertThat(result).hasSize(2);
  }

  @Test
  @DisplayName("Should handle order with multiple items")
  void testOrderWithMultipleItems() {
    // Given
    GolfBall secondBall = GolfBall.builder()
        .brand("Callaway")
        .model("Chrome Soft")
        .condition(BallCondition.GRADE_A)
        .quantity(50)
        .price(BigDecimal.valueOf(24.99))
        .build();
    entityManager.persist(secondBall);

    OrderItem secondItem = OrderItem.builder()
        .golfBall(secondBall)
        .quantity(5)
        .unitPrice(BigDecimal.valueOf(24.99))
        .build();
    testOrder.addItem(secondItem);

    // When
    orderRepository.save(testOrder);
    entityManager.flush();
    entityManager.clear();

    Order retrieved = orderRepository.findById(testOrder.getId()).orElse(null);

    // Then
    assertThat(retrieved).isNotNull();
    assertThat(retrieved.getItems()).hasSize(2);
    assertThat(retrieved.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(424.85));
  }
}
