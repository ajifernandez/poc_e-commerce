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

import poc.ecommerce.api.convert.ShoppingCartResourceAssembler;
import poc.ecommerce.api.exception.NotFoundException;
import poc.ecommerce.api.util.ResponseUtil;
import poc.ecommerce.model.Product;
import poc.ecommerce.model.Role;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.User;
import poc.ecommerce.model.response.ResponseHTTP;
import poc.ecommerce.service.SecurityService;
import poc.ecommerce.service.ShoppingCartService;

/**
 * Controller to shopping cart
 * 
 * @author Agustín
 *
 */
@RestController
@RequestMapping(path = "/shoppingcart")
public class ShoppingCartController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private ShoppingCartResourceAssembler shoppingcartResourceAssembler;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> retrieveAllShoppingCarts() {
		LOGGER.info("Request - Getting all shoppingCarts...");
		ResponseEntity<?> response;
		if (securityService.checkPermissions(Role.ROLE_ADMIN.name())) {
			final List<ShoppingCart> shoppingcart = shoppingCartService.getAllShoppingCarts();

			ResponseHTTP responseHTTP = new ResponseHTTP();
			responseHTTP.setStatus(HttpStatus.OK.value());
			responseHTTP.setValue(shoppingcart);
			return new ResponseEntity<>(responseHTTP, HttpStatus.OK);
		} else {
			response = ResponseUtil.createResponseUNAUTHORIZED();
		}
		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveShoppingCart(@PathVariable Long id) {
		LOGGER.info("Request - Get the shoppingcart by Id" + id);
		ResponseEntity<?> response = null;
		try {
			final ShoppingCart shoppingcart = shoppingCartService.getShoppingCartById(id)
					.orElseThrow(() -> new NotFoundException("shoppingcart"));
			if (securityService.checkPermissions(Role.ROLE_ADMIN.name())
					|| securityService.findLoggedInUsername().equals(shoppingcart.getUser().getUsername())) {
				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(shoppingcart);
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
	public ResponseEntity<?> createShoppingCart(@RequestBody ShoppingCartDto request) {
		LOGGER.info("Request - Creating the shoppingCart" + request);
		final ShoppingCart shoppingcart = shoppingCartService.createShoppingCart(request.getUser(),
				request.getProducts());
		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.CREATED.value());
		responseHTTP.setValue(shoppingcart);
		return new ResponseEntity<>(responseHTTP, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateShoppingCart(@PathVariable Long id, @RequestBody @Valid ShoppingCartDto request) {
		LOGGER.info("Request - Updating the shoppingCart" + request);
		ResponseEntity<?> response;
		try {
			final ShoppingCart shoppingcart = shoppingCartService.getShoppingCartById(id)
					.orElseThrow(() -> new NotFoundException("product"));
			if (securityService.checkPermissions(Role.ROLE_ADMIN.name())
					|| securityService.findLoggedInUsername().equals(shoppingcart.getUser().getUsername())) {
				shoppingcart.setProducts(request.getProducts());
				shoppingCartService.updateShoppingCart(shoppingcart);

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(shoppingcartResourceAssembler.toResource(shoppingcart));
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} else {
				response = ResponseUtil.createResponseUNAUTHORIZED();
			}
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNOT_FOUND(e);
		}
		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteShoppingCart(@PathVariable Long id) {
		LOGGER.info("Request - deleting shoppingcart...");
		ResponseEntity<?> response = null;
		try {
			final ShoppingCart shoppingcart = shoppingCartService.getShoppingCartById(id)
					.orElseThrow(() -> new NotFoundException("product"));
			if (securityService.checkPermissions(Role.ROLE_ADMIN.name())
					|| securityService.findLoggedInUsername().equals(shoppingcart.getUser().getUsername())) {
				shoppingCartService.deleteShoppingCart(shoppingcart);
				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.NO_CONTENT.value());
				responseHTTP.setValue(shoppingcart);
				response = new ResponseEntity<>(responseHTTP, HttpStatus.NO_CONTENT);
			} else {
				response = ResponseUtil.createResponseUNAUTHORIZED();
			}
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNOT_FOUND(e);
		}
		return response;
	}

	static class ShoppingCartDto {
		@NotNull(message = "user is required")
		private User user;
		private List<Product> products;

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public List<Product> getProducts() {
			return products;
		}

		public void setProducts(List<Product> products) {
			this.products = products;
		}

	}

}