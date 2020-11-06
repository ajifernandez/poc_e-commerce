package poc.ecommerce.api;

import java.util.List;
import java.util.Set;

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
import poc.ecommerce.model.Product;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.User;
import poc.ecommerce.model.response.ResponseHTTP;
import poc.ecommerce.service.ShoppingCartService;

@RestController
@RequestMapping(path = "/shoppingcart")
public class ShoppingCartController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private ShoppingCartResourceAssembler shoppingcartResourceAssembler;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> retrieveAllProducts() {
		final List<ShoppingCart> shoppingcart = shoppingCartService.getAllShoppingCarts();

		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setPath("/shoppingcart");
		responseHTTP.setStatus(HttpStatus.OK.value());
		responseHTTP.setValue(shoppingcart);
		return new ResponseEntity<>(responseHTTP, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveProduct(@PathVariable Long id) {
		final ShoppingCart shoppingcart = shoppingCartService.getShoppingCartById(id)
				.orElseThrow(() -> new NotFoundException("product"));
		return ResponseEntity.ok(shoppingcart);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createShoppingCart(@RequestBody ShoppingCartDto request) {
		final ShoppingCart shoppingcart = shoppingCartService.createShoppingCart(request.getUser(), request.getProducts());
		return ResponseEntity.status(HttpStatus.CREATED).body(shoppingcart);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ShoppingCartDto request) {
		final ShoppingCart shoppingcart = shoppingCartService.getShoppingCartById(id)
				.orElseThrow(() -> new NotFoundException("product"));

		shoppingCartService.updateShoppingCart(shoppingcart);

		return ResponseEntity.ok(shoppingcartResourceAssembler.toResource(shoppingcart));
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteShoppingCart(@PathVariable Long id) {
		final ShoppingCart shoppingcart = shoppingCartService.getShoppingCartById(id)
				.orElseThrow(() -> new NotFoundException("product"));

		shoppingCartService.deleteShoppingCart(shoppingcart);

		return ResponseEntity.noContent().build();
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