package poc.ecommerce.service;

import java.util.Optional;

import poc.ecommerce.model.ShoppingCart;

public interface ShoppingCartService {

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

	ShoppingCart getShoppingCartByUsername(String findLoggedInUsername);

}