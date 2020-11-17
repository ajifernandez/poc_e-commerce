package poc.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import poc.ecommerce.model.ProductsInProcess;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.User;
import poc.ecommerce.repository.ShoppingCartRepository;
import poc.ecommerce.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        List<ProductsInProcess> products = new ArrayList<ProductsInProcess>();
		shoppingCart.setProducts(products);
        shoppingCart.setUser(user);
		shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
