package bbw.ch.FairwayEcoBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a customer order.
 */
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Customer is required")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<OrderItem> items = new ArrayList<>();

  @NotNull(message = "Order status is required")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private OrderStatus status = OrderStatus.PENDING;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal totalAmount;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "street", column = @Column(name = "shipping_street")),
      @AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
      @AttributeOverride(name = "postalCode", column = @Column(name = "shipping_postal_code")),
      @AttributeOverride(name = "country", column = @Column(name = "shipping_country"))
  })
  private Address shippingAddress;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
    calculateTotalAmount();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
    calculateTotalAmount();
  }

  public void addItem(OrderItem item) {
    items.add(item);
    item.setOrder(this);
    calculateTotalAmount();
  }

  public void removeItem(OrderItem item) {
    items.remove(item);
    item.setOrder(null);
    calculateTotalAmount();
  }

  private void calculateTotalAmount() {
    this.totalAmount = items.stream()
        .map(OrderItem::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
