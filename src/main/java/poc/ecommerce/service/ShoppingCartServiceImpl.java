package poc.ecommerce.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.repository.ShoppingCartRepository;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private ShoppingCartRepository shoppingcartRepository;

	@Transactional
	@Override
	public Optional<ShoppingCart> getShoppingCartById(Long id) {
		return shoppingcartRepository.findById(id);
	}

	@Transactional
	@Override
	public void updateShoppingCart(ShoppingCart shoppingcart) {
		shoppingcartRepository.save(shoppingcart);
	}

	@Override
	public ShoppingCart getShoppingCartByUsername(String username) {
		ShoppingCart result = null;
		for (ShoppingCart shoppingCart : shoppingcartRepository.findAll()) {
			if (shoppingCart.getUser().getUsername().equals(username)) {
				result = shoppingCart;
			}

		}
		return result;
	}

}