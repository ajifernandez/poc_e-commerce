package poc.ecommerce.service;

import java.util.List;
import java.util.Optional;

import poc.ecommerce.model.Product;

public interface ProductService {

	/**
	 * Gets all products present in the system. The result is paginated.
	 *
	 * @param page the page to fetch results from
	 * @return the paginated results
	 */
	List<Product> getAllProducts();

	/**
	 * Gets a specific product by looking for its id.
	 *
	 * @param id the id of the product to look for
	 * @return the product (if any)
	 */
	Optional<Product> getProductById(Long id);

	List<Product> getProductByName(String infix, Double price);

	/**
	 * Creates a product. If the currency is not 'EUR' then a Currency Exchange will
	 * be performed.
	 *
	 * @param name     the name of the product
	 * @param currency the currency of the product
	 * @param price    the price of the product
	 * @return the new product
	 */
	Product createProduct(String name, String currency, double price);

	/**
	 * Updates an existing product. If the currency is not 'EUR' then a Currency
	 * Exchange will be performed.
	 *
	 * @param product  the product to update
	 * @param name     the new product name
	 * @param currency the new product currency
	 * @param price    the new product price
	 */
	void updateProduct(Product product, String name, String currency, double price);

	/**
	 * Deletes a product in the system.
	 *
	 * @param product the product to delete
	 */
	void deleteProduct(Product product);

}