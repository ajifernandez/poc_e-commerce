package poc.ecommerce.service;

import java.util.List;
import java.util.Optional;

import poc.ecommerce.model.Product;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.User;

public interface ShoppingCartService {

	List<ShoppingCart> getAllShoppingCarts();

	Optional<ShoppingCart> getShoppingCartById(Long id);

	ShoppingCart createShoppingCart(User user, List<Product> products);

	void updateShoppingCart(ShoppingCart shoppingcart);

	void deleteShoppingCart(ShoppingCart shoppingcart);

}