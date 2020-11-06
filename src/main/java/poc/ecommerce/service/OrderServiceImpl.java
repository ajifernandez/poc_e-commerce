package poc.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poc.ecommerce.model.BillingInfo;
import poc.ecommerce.model.Order;
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
	public Order createOrder(User user, Long shoppingcartId, BillingInfo billingInfo) {
		Order order = new Order();
		order.setUser(user);
		order.setProducts(new ArrayList<>(shoppingcartRepository.findById(shoppingcartId).get().getProducts()));
		order.setCardName(billingInfo.getCardName());
		order.setCardNumber(billingInfo.getCardNumber());
		order.setDates(billingInfo.getDates());
		order.setStatus("CREATED");
		order.setAdditionalInfo("");
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

}