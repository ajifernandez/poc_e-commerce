package poc.ecommerce.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import poc.ecommerce.api.exception.NotFoundException;
import poc.ecommerce.api.model.ProductDto;
import poc.ecommerce.api.util.ResponseUtil;
import poc.ecommerce.model.Product;
import poc.ecommerce.model.response.ResponseHTTP;
import poc.ecommerce.service.ProductService;

/**
 * ProductController
 * 
 * @author Agustín
 *
 */
@RestController
@PreAuthorize("isFullyAuthenticated()")
@RequestMapping(path = "/catalog")
public class ProductController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> retrieveAllProducts() {
		LOGGER.info("Request - Getting all products");
		
		List<Product> products = productService.getAllProducts();

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
		  .setFieldMatchingEnabled(true)
		  .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

		List<ProductDto> productsDto = modelMapper.map(products, new TypeToken<List<ProductDto>>() {}.getType());

		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.OK.value());
		responseHTTP.setValue(productsDto);
		ResponseEntity<?> response= new ResponseEntity<>(responseHTTP, HttpStatus.OK);
		return response;
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
			response = ResponseUtil.createResponseNotFound(e);
		}
		return response;
	}

	@RequestMapping(path = "/filter", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveProductByName(@RequestParam(required = false) String name,
			@RequestParam(required = false) Double price) {
		LOGGER.info("Request - Get the services filtering by " + name + "and price " + price);
		ResponseEntity<?> response;
		final List<Product> products = productService.getFilterProduct(name, price);

		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.OK.value());
		responseHTTP.setValue(products);
		response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
		return response;
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto request) {
		ResponseEntity<?> response;
		LOGGER.info("Request - Creating a new product...");
		final Product product = productService.createProduct(request.getName(), Product.CURRENCY, request.getPrice());
		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.CREATED.value());
		responseHTTP.setValue(product);
		response = new ResponseEntity<>(responseHTTP, HttpStatus.CREATED);

		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto request) {
		ResponseEntity<?> response;
		LOGGER.info("Request - updating a product...");
		try {
			// Getting the requiring product; or throwing exception if not found
			final Product product = productService.getProductById(id)
					.orElseThrow(() -> new NotFoundException("product"));
			productService.updateProduct(product, request.getName(), Product.CURRENCY, request.getPrice());

			ResponseHTTP responseHTTP = new ResponseHTTP();
			responseHTTP.setStatus(HttpStatus.OK.value());
			responseHTTP.setValue(product);
			response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNotFound(e);
		}
		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		LOGGER.info("Request - deleting a product...");
		ResponseEntity<?> response = null;
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
			response = ResponseUtil.createResponseNotFound(e);
		} catch (DataIntegrityViolationException e) {
			response = ResponseUtil.createResponseExpectationFailed(e);
		}
		return response;

	}

}