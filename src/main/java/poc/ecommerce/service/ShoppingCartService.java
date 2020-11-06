package poc.ecommerce.service;

import java.util.List;
import java.util.Optional;

import poc.ecommerce.model.Product;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.User;

public interface ShoppingCartService {

	/**
	 * Retrieve all the shoppingCarts present in the system
	 * 
	 * @return all the shoppingCarts
	 */
	List<ShoppingCart> getAllShoppingCarts();

	/**
	 * Gets a specific shoppingCart by looking for its id.
	 *
	 * @param id the id of the shoppingCart to look for
	 * @return the shoppingCart (if any)
	 */
	Optional<ShoppingCart> getShoppingCartById(Long id);

	/**
	 * Creates a shoppingCart
	 * 
	 * @param user     the user that create
	 * @param products the list of products
	 * @return the new shoppingCart
	 */
	ShoppingCart createShoppingCart(User user, List<Product> products);

	/**
	 * Updates the shoppingCart
	 * 
	 * @param shoppingcart the shoppingCart to update
	 */
	void updateShoppingCart(ShoppingCart shoppingcart);

	/**
	 * Deletes the shoppingCart
	 * 
	 * @param shoppingcart the shoppingCart to be deleted
	 */
	void deleteShoppingCart(ShoppingCart shoppingcart);

}