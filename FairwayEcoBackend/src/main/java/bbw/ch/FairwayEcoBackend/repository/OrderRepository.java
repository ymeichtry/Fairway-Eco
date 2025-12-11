package bbw.ch.FairwayEcoBackend.repository;

import bbw.ch.FairwayEcoBackend.model.Order;
import bbw.ch.FairwayEcoBackend.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Order entity operations.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByCustomerId(Long customerId);

  List<Order> findByStatus(OrderStatus status);

  List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);

  @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
  List<Order> findOrdersBetweenDates(
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

  @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
  Long countByStatus(@Param("status") OrderStatus status);
}
