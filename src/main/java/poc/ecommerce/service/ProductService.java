package poc.ecommerce.service;

import java.util.List;
import java.util.Optional;

import poc.ecommerce.model.Product;

public interface ProductService {

	/**
	 * Gets all products present in the system
	 *
	 * @return all the products
	 */
	List<Product> getAllProducts();

	/**
	 * Gets a specific product by looking for its id.
	 *
	 * @param id the id of the product to look for
	 * @return the product (if any)
	 */
	Optional<Product> getProductById(Long id);

	/**
	 * Gets all products present in the system that match with the filter
	 * 
	 * @param infix the string contained in the product name
	 * @param price maximum price of the product
	 * @return the filtered products
	 */
	List<Product> getFilterProduct(String infix, Double price);

	/**
	 * Creates a product. 
	 * 
	 * @param name     the name of the product
	 * @param currency the currency of the product
	 * @param price    the price of the product
	 * @return the new product
	 */
	Product createProduct(String name, String currency, double price);

	/**
	 * Updates an existing product.
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