package poc.ecommerce.service;

import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import poc.ecommerce.model.Product;
import poc.ecommerce.model.User;
import poc.ecommerce.repository.ProductRepository;

/**
 * Spring-oriented Implementation for {@link ProductService}
 *
 * @author dnardelli
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional
	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional
	@Override
	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional
	@Override
	public List<Product> getProductByName(String infix) {
		return productRepository.findByNameContaining(infix);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional
	@Override
	public Product createProduct(String name, String currency, double price) {
		// Round up only 2 decimals...
		price = (double) Math.round(price * 100) / 100;

		Product product = new Product();
		product.setName(name);
		product.setPrice(price);

		return productRepository.save(product);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #product.getUser().getId()")
	@Transactional
	@Override
	public void updateProduct(Product product, String name, String currency, double price) {
		// Round up only 2 decimals...
		price = (double) Math.round(price * 100) / 100;

		product.setName(name);
		product.setPrice(price);
		productRepository.save(product);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #product.getUser().getId()")
	@Transactional
	@Override
	public void deleteProduct(Product product) {
		productRepository.delete(product);
	}

}