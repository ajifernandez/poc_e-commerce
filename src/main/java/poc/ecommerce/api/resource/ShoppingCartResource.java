package poc.ecommerce.api.resource;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import poc.ecommerce.model.Product;
import poc.ecommerce.model.User;

public class ShoppingCartResource extends ResourceSupport {

	User user;
	List<Product> products;

	public ShoppingCartResource(User _user, List<Product> _products) {
		user = _user;
		products = _products;
	}

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