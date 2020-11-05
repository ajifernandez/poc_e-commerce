package poc.ecommerce.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import poc.ecommerce.api.convert.ProductResourceAssembler;
import poc.ecommerce.api.exception.NotFoundException;
import poc.ecommerce.model.Product;
import poc.ecommerce.model.response.ResponseHTTP;
import poc.ecommerce.service.ProductService;

/**
 * API Endpoint for product management
 *
 * @author dnardelli
 */
@RestController
@RequestMapping(path = "/catalog")
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductResourceAssembler productResourceAssembler;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> retrieveAllProducts() {
		// Getting all products in application...
		final List<Product> products = productService.getAllProducts();
//		return ResponseEntity.ok(products);
		
		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setPath("/catalog");
		responseHTTP.setStatus(HttpStatus.OK.value());
		responseHTTP.setValue(products);
		return new ResponseEntity<>(responseHTTP, HttpStatus.OK);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveProduct(@PathVariable Long id) {
		// Getting the requiring product; or throwing exception if not found
		final Product product = productService.getProductById(id).orElseThrow(() -> new NotFoundException("product"));

		return ResponseEntity.ok(product);
	}
	
	@RequestMapping(path = "/infix/{infix}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveProductByName(@PathVariable String infix) {
		return ResponseEntity.ok(productService.getProductByName(infix));
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDto request) {
		// Creating a new product in the application...
		final Product product = productService.createProduct(request.getName(), request.getCurrency(),
				request.getPrice());

		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDto request) {
		// Getting the requiring product; or throwing exception if not found
		final Product product = productService.getProductById(id).orElseThrow(() -> new NotFoundException("product"));

		// Updating a product in the application...
		productService.updateProduct(product, request.getName(), request.getCurrency(), request.getPrice());

		return ResponseEntity.ok(productResourceAssembler.toResource(product));
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		// Getting the requiring product; or throwing exception if not found
		final Product product = productService.getProductById(id).orElseThrow(() -> new NotFoundException("product"));

		// Deleting product from the application...
		productService.deleteProduct(product);

		return ResponseEntity.noContent().build();
	}

	static class ProductDto {
		@NotNull(message = "name is required")
		@Size(message = "name must be equal to or lower than 300", min = 1, max = 300)
		private String name;
		@NotNull
		@Size(message = "Currency must be in ISO 4217 format", min = 3, max = 3)
		private String currency;
		@NotNull(message = "name is required")
		@Min(0)
		private Double price;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}
	}

}