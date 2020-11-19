package poc.ecommerce.api;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import poc.ecommerce.api.exception.NotFoundException;
import poc.ecommerce.api.model.OrderDto;
import poc.ecommerce.api.model.ProductDto;
import poc.ecommerce.api.model.ShoppingCartDto;
import poc.ecommerce.api.util.ResponseUtil;
import poc.ecommerce.model.Order;
import poc.ecommerce.model.Role;
import poc.ecommerce.model.response.ResponseHTTP;
import poc.ecommerce.service.OrderService;
import poc.ecommerce.service.SecurityService;
import poc.ecommerce.service.UserService;

/**
 * OrderController
 * 
 * @author Agustín
 *
 */
@RestController
@PreAuthorize("isFullyAuthenticated()")
@RequestMapping(path = "/order")
public class OrderController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityService securityService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> retrieveAllOrders() {
		LOGGER.info("Request - Getting all orders");
		final List<Order> orders = orderService.getAllOrders();
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFieldMatchingEnabled(true)
				.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

		List<OrderDto> ordersDto = modelMapper.map(orders, new TypeToken<List<OrderDto>>() {
		}.getType());

		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.OK.value());
		responseHTTP.setValue(ordersDto);
		return new ResponseEntity<>(responseHTTP, HttpStatus.OK);
	}

	@RequestMapping(path = "/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveOrders(@PathVariable String username) {
		LOGGER.info("Request - Getting order by username: " + username);
		ResponseEntity<?> response = null;
		final List<Order> orders = orderService.getOrdersByUsername(username);
		if (orders != null && (username.equals(securityService.findLoggedInUsername())
				|| securityService.checkPermissions(Role.ROLE_ADMIN.name()))) {
			ModelMapper modelMapper = new ModelMapper();
			List<ProductDto> productsDto = modelMapper.map(orders, new TypeToken<List<OrderDto>>() {
			}.getType());
			ResponseHTTP responseHTTP = new ResponseHTTP();
			responseHTTP.setStatus(HttpStatus.OK.value());
			responseHTTP.setValue(productsDto);
			response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
		} else {
			response = ResponseUtil.createResponseUnauthorized();
		}
		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveOrder(@PathVariable Long id) {
		LOGGER.info("Request - Getting order by id: " + id);
		ResponseEntity<?> response = null;
		try {
			final Order order = orderService.getOrderById(id).orElseThrow(() -> new NotFoundException("order"));
			if (order != null && (order.getUser().getUsername().equals(securityService.findLoggedInUsername())
					|| securityService.checkPermissions(Role.ROLE_ADMIN.name()))) {
				ModelMapper modelMapper = new ModelMapper();
				OrderDto orderDTO = modelMapper.map(order, OrderDto.class);

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(orderDTO);
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} else {
				response = ResponseUtil.createResponseUnauthorized();
			}
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNotFound(e);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto) {
		LOGGER.info("Request - Creating order: " + orderDto);

		final Order order = orderService.createOrderAndClearShoppingCart(
				userService.findByUsername(orderDto.getUser().getUsername()), orderDto.getShoppingcartId(),
				orderDto.getBillingInfo());

		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.CREATED.value());
		responseHTTP.setValue(order);
		return new ResponseEntity<>(responseHTTP, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/checkout/{id}", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> checkoutOrder(@PathVariable Long id) {
		ResponseEntity<?> response;
		LOGGER.info("Request - CheckinOut an order...");
		try {
			final Order order = orderService.getOrderById(id).orElseThrow(() -> new NotFoundException("order"));
			order.setStatus("PROCESSED");
			orderService.updateOrder(order);

			ResponseHTTP responseHTTP = new ResponseHTTP();
			responseHTTP.setStatus(HttpStatus.OK.value());
			responseHTTP.setValue(order);
			response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNotFound(e);
		}
		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody @Valid ShoppingCartDto request) {
		ResponseEntity<?> response;
		LOGGER.info("Request - CheckinOut an order...");

		try {
			final Order order = orderService.getOrderById(id).orElseThrow(() -> new NotFoundException("order"));
			if (order != null && (order.getUser().getUsername().equals(securityService.findLoggedInUsername())
					|| securityService.checkPermissions(Role.ROLE_ADMIN.name()))) {
				orderService.updateOrder(order);

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(order);
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} else {
				response = ResponseUtil.createResponseUnauthorized();
			}
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNotFound(e);
		}
		return response;
	}

	@RequestMapping(path = "/cancel/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
		ResponseEntity<?> response;
		LOGGER.info("Request - CheckinOut an order...");
		try {
			final Order order = orderService.getOrderById(id).orElseThrow(() -> new NotFoundException("order"));
			if (order != null && (order.getUser().getUsername().equals(securityService.findLoggedInUsername())
					|| securityService.checkPermissions(Role.ROLE_ADMIN.name()))) {
				order.setStatus("CANCELLED");
				orderService.updateOrder(order);

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(order);
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} else {
				response = ResponseUtil.createResponseUnauthorized();
			}
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNotFound(e);
		}
		return response;
	}

}