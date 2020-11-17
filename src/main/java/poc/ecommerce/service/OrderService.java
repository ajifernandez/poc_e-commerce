package poc.ecommerce.service;

import java.util.List;
import java.util.Optional;

import poc.ecommerce.api.model.BillingInfoDto;
import poc.ecommerce.model.Order;
import poc.ecommerce.model.User;

public interface OrderService {

	List<Order> getAllOrders();

	Optional<Order> getOrderById(Long id);

	Order createOrderAndClearShoppingCart(User user, Long shoppingcartId, BillingInfoDto billingInfo);

	void updateOrder(Order order);

	void deleteOrder(Order order);

}