package poc.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poc.ecommerce.api.model.BillingInfoDto;
import poc.ecommerce.model.Order;
import poc.ecommerce.model.ProductsInProcess;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.User;
import poc.ecommerce.repository.OrderRepository;
import poc.ecommerce.repository.ShoppingCartRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ShoppingCartRepository shoppingcartRepository;

	@Transactional
	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Transactional
	@Override
	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}

	@Transactional
	@Override
	public Order createOrderAndClearShoppingCart(User user, Long shoppingcartId, BillingInfoDto billingInfo) {
		Order order = new Order();
		order.setUser(user);
		order.setProducts(shoppingcartRepository.findById(shoppingcartId).get().getProducts());
		order.setCardName(billingInfo.getCardName());
		order.setCardNumber(billingInfo.getCardNumber());
		order.setDates(billingInfo.getDates());
		order.setStatus("CREATED");
		order.setAdditionalInfo("");

		ShoppingCart shoppingCart = shoppingcartRepository.findById(shoppingcartId).get();
		shoppingCart.setProducts(new ArrayList<ProductsInProcess>());
		shoppingcartRepository.save(shoppingcartRepository.findById(shoppingcartId).get());
		
		return orderRepository.save(order);
	}

	@Transactional
	@Override
	public void updateOrder(Order order) {
		orderRepository.save(order);
	}

	@Transactional
	@Override
	public void deleteOrder(Order order) {
		orderRepository.delete(order);
	}

	@Override
	public List<Order> getOrdersByUsername(String username) {
		List<Order> result = new ArrayList<Order>();
		for (Order order : orderRepository.findAll()) {
			if (order.getUser().getUsername().equals(username)) {
				result.add(order);
			}
		}
		return result;
	}

}