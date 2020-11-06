package poc.ecommerce.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poc.ecommerce.api.convert.ProductResourceAssembler;
import poc.ecommerce.api.exception.NotFoundException;
import poc.ecommerce.api.util.ResponseUtil;
import poc.ecommerce.model.Product;
import poc.ecommerce.model.Role;
import poc.ecommerce.model.response.ResponseHTTP;
import poc.ecommerce.service.ProductService;
import poc.ecommerce.service.SecurityService;

/**
 * ProductController
 * 
 * @author Agustín
 *
 */
@RestController
@RequestMapping(path = "/catalog")
public class ProductController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private ProductResourceAssembler productResourceAssembler;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> retrieveAllProducts() {
		LOGGER.info("Request - Getting all products");

		final List<Product> products = productService.getAllProducts();

		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.OK.value());
		responseHTTP.setValue(products);
		return new ResponseEntity<>(responseHTTP, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveProduct(@PathVariable Long id) {
		LOGGER.info("Request - Get the product by Id" + id);
		ResponseEntity<?> response = null;
		try {
			final Product product = productService.getProductById(id)
					.orElseThrow(() -> new NotFoundException("product"));
			ResponseHTTP responseHTTP = new ResponseHTTP();
			responseHTTP.setStatus(HttpStatus.OK.value());
			responseHTTP.setValue(product);
			response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNOT_FOUND(e);
		}
		return response;
	}

	@RequestMapping(path = "/filter", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveProductByName(@RequestParam(required = false) String name,
			@RequestParam(required = false) Double price) {
		LOGGER.info("Request - Get the services filtering by " + name + "and price " + price);
		return ResponseEntity.ok(productService.getProductByName(name, price));
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto request) {
		ResponseEntity<?> response;
		LOGGER.info("Request - Creating a new product...");
		if (securityService.checkPermissions(Role.ROLE_ADMIN.name())) {
			final Product product = productService.createProduct(request.getName(), request.getCurrency(),
					request.getPrice());
			ResponseHTTP responseHTTP = new ResponseHTTP();
			responseHTTP.setStatus(HttpStatus.CREATED.value());
			responseHTTP.setValue(product);
			response = new ResponseEntity<>(responseHTTP, HttpStatus.CREATED);
		} else {
			response = ResponseUtil.createResponseUNAUTHORIZED();
		}

		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto request) {
		ResponseEntity<?> response;
		if (securityService.checkPermissions(Role.ROLE_ADMIN.name())) {
			LOGGER.info("Request - updating a product...");
			try {
				// Getting the requiring product; or throwing exception if not found
				final Product product = productService.getProductById(id)
						.orElseThrow(() -> new NotFoundException("product"));
				productService.updateProduct(product, request.getName(), request.getCurrency(), request.getPrice());

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(productResourceAssembler.toResource(product));
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} catch (NotFoundException e) {
				response = ResponseUtil.createResponseNOT_FOUND(e);
			}
		} else {
			response = ResponseUtil.createResponseUNAUTHORIZED();
		}
		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		LOGGER.info("Request - deleting a product...");
		ResponseEntity<?> response = null;
		if (securityService.checkPermissions(Role.ROLE_ADMIN.name())) {
			try {
				// Getting the requiring product; or throwing exception if not found
				final Product product = productService.getProductById(id)
						.orElseThrow(() -> new NotFoundException("product"));

				// Deleting product from the application...
				productService.deleteProduct(product);

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.NO_CONTENT.value());
				responseHTTP.setValue(product);
				response = new ResponseEntity<>(responseHTTP, HttpStatus.NO_CONTENT);
			} catch (NotFoundException e) {
				response = ResponseUtil.createResponseNOT_FOUND(e);
			}
		} else {
			response = ResponseUtil.createResponseUNAUTHORIZED();
		}
		return response;

	}

	static class ProductDto {
		@NotNull(message = "name is required")
		@Size(message = "name must be equal to or lower than 300", min = 1, max = 300)
		private String name;

		@NotNull
		@Size(message = "Currency must be in ISO 4217 format", min = 3, max = 3)
		private String currency;

		@NotNull(message = "price is required")
		@Min(0)
		private Double price;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the currency
		 */
		public String getCurrency() {
			return currency;
		}

		/**
		 * @param currency the currency to set
		 */
		public void setCurrency(String currency) {
			this.currency = currency;
		}

		/**
		 * @return the price
		 */
		public Double getPrice() {
			return price;
		}

		/**
		 * @param price the price to set
		 */
		public void setPrice(Double price) {
			this.price = price;
		}

	}

}