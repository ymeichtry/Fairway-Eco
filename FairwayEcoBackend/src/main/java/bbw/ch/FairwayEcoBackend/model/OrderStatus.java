package bbw.ch.FairwayEcoBackend.model;

/**
 * Enum representing the status of an order.
 */
public enum OrderStatus {
  PENDING("Order received, awaiting payment"),
  PAID("Payment confirmed"),
  PROCESSING("Order is being prepared"),
  SHIPPED("Order has been shipped"),
  DELIVERED("Order has been delivered"),
  CANCELLED("Order has been cancelled"),
  REFUNDED("Order has been refunded");

  private final String description;

  OrderStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
