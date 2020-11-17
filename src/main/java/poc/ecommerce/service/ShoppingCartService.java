package poc.ecommerce.service;

import java.util.List;
import java.util.Optional;

import poc.ecommerce.model.ShoppingCart;

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

	ShoppingCart getShoppingCartByUsername(String findLoggedInUsername);

}