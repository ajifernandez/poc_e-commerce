package poc.ecommerce.api.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import poc.ecommerce.model.User;

public class OrderDto {
	private Long id;
	@NotNull(message = "user is required")
	private User user;
	private BillingInfoDto billingInfo;
	private Long shoppingcartId;

	private List<ProductsInProcessDto> products;
	private String status;
	private String additionalInfo;

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

	public Long getShoppingcartId() {
		return shoppingcartId;
	}

	public void setShoppingcartId(Long shoppingcartId) {
		this.shoppingcartId = shoppingcartId;
	}

	/**
	 * @return the billingInfo
	 */
	public BillingInfoDto getBillingInfo() {
		return billingInfo;
	}

	/**
	 * @param billingInfo the billingInfo to set
	 */
	public void setBillingInfo(BillingInfoDto billingInfo) {
		this.billingInfo = billingInfo;
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

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the additionalInfo
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	/**
	 * @param additionalInfo the additionalInfo to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

}