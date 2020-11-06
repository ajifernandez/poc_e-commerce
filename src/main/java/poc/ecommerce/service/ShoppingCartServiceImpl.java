package poc.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import poc.ecommerce.model.Product;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.User;
import poc.ecommerce.repository.ShoppingCartRepository;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private ShoppingCartRepository shoppingcartRepository;

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional
	@Override
	public List<ShoppingCart> getAllShoppingCarts() {
		return shoppingcartRepository.findAll();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional
	@Override
	public Optional<ShoppingCart> getShoppingCartById(Long id) {
		return shoppingcartRepository.findById(id);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional
	@Override
	public ShoppingCart createShoppingCart(User user, List<Product> products) {
		ShoppingCart shoppingcart = new ShoppingCart();
		shoppingcart.setUser(user);
		shoppingcart.setProducts(products);

		return shoppingcartRepository.save(shoppingcart);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #product.getUser().getId()")
	@Transactional
	@Override
	public void updateShoppingCart(ShoppingCart shoppingcart) {
		shoppingcartRepository.save(shoppingcart);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #product.getUser().getId()")
	@Transactional
	@Override
	public void deleteShoppingCart(ShoppingCart shoppingcart) {
		shoppingcartRepository.delete(shoppingcart);
	}

}