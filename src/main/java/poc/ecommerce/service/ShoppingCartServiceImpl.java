package poc.ecommerce.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poc.ecommerce.model.Product;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.User;
import poc.ecommerce.repository.ShoppingCartRepository;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private ShoppingCartRepository shoppingcartRepository;

	@Transactional
	@Override
	public List<ShoppingCart> getAllShoppingCarts() {
		return shoppingcartRepository.findAll();
	}

	@Transactional
	@Override
	public Optional<ShoppingCart> getShoppingCartById(Long id) {
		return shoppingcartRepository.findById(id);
	}

	@Transactional
	@Override
	public ShoppingCart createShoppingCart(User user, List<Product> products) {
		ShoppingCart shoppingcart = new ShoppingCart();
		shoppingcart.setUser(user);
		shoppingcart.setProducts(products);
		ShoppingCart result = shoppingcartRepository.save(shoppingcart);
		shoppingcartRepository.flush();
		return result;
	}

	@Transactional
	@Override
	public void updateShoppingCart(ShoppingCart shoppingcart) {
		shoppingcartRepository.save(shoppingcart);
	}

	@Transactional
	@Override
	public void deleteShoppingCart(ShoppingCart shoppingcart) {
		shoppingcartRepository.delete(shoppingcart);
	}

}