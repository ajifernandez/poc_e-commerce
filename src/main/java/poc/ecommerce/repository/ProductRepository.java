package poc.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poc.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByNameContaining(String infix);
}