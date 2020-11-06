package poc.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poc.ecommerce.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}