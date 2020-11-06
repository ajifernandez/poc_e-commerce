package poc.ecommerce.api.resource;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import poc.ecommerce.model.BillingInfo;
import poc.ecommerce.model.Product;
import poc.ecommerce.model.User;

public class OrderResource extends ResourceSupport {

	private User user;

	private BillingInfo billingInfo;

	private List<Product> products;

	private String status;
	private String additionalInfo;

	public OrderResource(User _user, BillingInfo _billingInfo, List<Product> _products, String _status,
			String _additionalInfo) {
		user = _user;
		billingInfo = _billingInfo;
		products = _products;
		status = _status;
		additionalInfo = _additionalInfo;
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

	public BillingInfo getBillingInfo() {
		return billingInfo;
	}

	public void setBillingInfo(BillingInfo billingInfo) {
		this.billingInfo = billingInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

}