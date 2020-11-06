package poc.ecommerce.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import poc.ecommerce.api.ShoppingCartController.ShoppingCartDto;
import poc.ecommerce.api.convert.OrderResourceAssembler;
import poc.ecommerce.api.exception.NotFoundException;
import poc.ecommerce.api.util.ResponseUtil;
import poc.ecommerce.model.BillingInfo;
import poc.ecommerce.model.Order;
import poc.ecommerce.model.Role;
import poc.ecommerce.model.User;
import poc.ecommerce.model.response.ResponseHTTP;
import poc.ecommerce.service.OrderService;
import poc.ecommerce.service.SecurityService;
import poc.ecommerce.service.ShoppingCartService;

/**
 * OrderController
 * 
 * @author Agustín
 *
 */
@RestController
@RequestMapping(path = "/order")
public class OrderController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private OrderResourceAssembler orderResourceAssembler;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> retrieveAllOrders() {
		LOGGER.info("Request - Getting all orders");
		ResponseEntity<?> response = null;
		if (securityService.checkPermissions(Role.ROLE_ADMIN.name())) {
			final List<Order> orders = orderService.getAllOrders();

			ResponseHTTP responseHTTP = new ResponseHTTP();
			responseHTTP.setStatus(HttpStatus.OK.value());
			responseHTTP.setValue(orders);
			return new ResponseEntity<>(responseHTTP, HttpStatus.OK);
		} else {
			response = ResponseUtil.createResponseUNAUTHORIZED();
		}
		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveOrder(@PathVariable Long id) {
		LOGGER.info("Request - Getting order by id: " + id);
		ResponseEntity<?> response = null;
		try {
			final Order order = orderService.getOrderById(id).orElseThrow(() -> new NotFoundException("order"));
			if (order != null && order.getUser().getUsername().equals(securityService.findLoggedInUsername())) {
				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(order);
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} else {
				response = ResponseUtil.createResponseUNAUTHORIZED();
			}
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNOT_FOUND(e);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createOrder(@RequestBody OrderDto request) {
		LOGGER.info("Request - Creating order: " + request);
		final Order order = orderService.createOrder(request.getUser(), request.getShoppingcartId(),
				request.getBillingInfo());

		shoppingCartService
				.deleteShoppingCart(shoppingCartService.getShoppingCartById(request.getShoppingcartId()).get());

		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.CREATED.value());
		responseHTTP.setValue(order);
		return new ResponseEntity<>(responseHTTP, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/checkout/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> checkoutOrder(@PathVariable Long id) {
		ResponseEntity<?> response;
		LOGGER.info("Request - CheckinOut an order...");
		if (securityService.checkPermissions(Role.ROLE_ADMIN.name())) {
			try {
				final Order order = orderService.getOrderById(id).orElseThrow(() -> new NotFoundException("order"));
				order.setStatus("PROCESSED");
				orderService.updateOrder(order);

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(orderResourceAssembler.toResource(order));
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} catch (NotFoundException e) {
				response = ResponseUtil.createResponseNOT_FOUND(e);
			}
		} else {
			response = ResponseUtil.createResponseUNAUTHORIZED();
		}
		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody @Valid ShoppingCartDto request) {
		ResponseEntity<?> response;
		LOGGER.info("Request - CheckinOut an order...");
		if (securityService.checkPermissions(Role.ROLE_ADMIN.name())) {
			try {
				final Order order = orderService.getOrderById(id).orElseThrow(() -> new NotFoundException("order"));
				orderService.updateOrder(order);

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(orderResourceAssembler.toResource(order));
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} catch (NotFoundException e) {
				response = ResponseUtil.createResponseNOT_FOUND(e);
			}
		} else {
			response = ResponseUtil.createResponseUNAUTHORIZED();
		}
		return response;
	}

	@RequestMapping(path = "/cancel/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
		ResponseEntity<?> response;
		LOGGER.info("Request - CheckinOut an order...");
		if (securityService.checkPermissions(Role.ROLE_ADMIN.name())) {
			try {
				final Order order = orderService.getOrderById(id).orElseThrow(() -> new NotFoundException("order"));
				order.setStatus("CANCELLED");
				orderService.updateOrder(order);

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(orderResourceAssembler.toResource(order));
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} catch (NotFoundException e) {
				response = ResponseUtil.createResponseNOT_FOUND(e);
			}
		} else {
			response = ResponseUtil.createResponseUNAUTHORIZED();
		}
		return response;
	}

	static class OrderDto {
		@NotNull(message = "user is required")
		private User user;
		private BillingInfo billingInfo;
		private Long shoppingcartId;

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Long getShoppingcartId() {
			return shoppingcartId;
		}

		public void setShoppingcartId(Long shoppingcartId) {
			this.shoppingcartId = shoppingcartId;
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}

		public void setBillingInfo(BillingInfo billingInfo) {
			this.billingInfo = billingInfo;
		}

	}

}