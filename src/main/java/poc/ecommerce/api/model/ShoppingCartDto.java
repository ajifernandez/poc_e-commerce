package poc.ecommerce.api.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import poc.ecommerce.model.User;

public class ShoppingCartDto {
	private Long id;
	@NotNull(message = "user is required")
	private User user;
	private List<ProductsInProcessDto> products;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the products
	 */
	public List<ProductsInProcessDto> getProducts() {
		return products;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(List<ProductsInProcessDto> products) {
		this.products = products;
	}

}