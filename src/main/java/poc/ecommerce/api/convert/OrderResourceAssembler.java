package poc.ecommerce.api.convert;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import poc.ecommerce.api.OrderController;
import poc.ecommerce.api.resource.OrderResource;
import poc.ecommerce.model.BillingInfo;
import poc.ecommerce.model.Order;

@Component
public class OrderResourceAssembler extends ResourceAssemblerSupport<Order, OrderResource> {

	public OrderResourceAssembler() {
		super(OrderController.class, OrderResource.class);
	}

	@Override
	protected OrderResource instantiateResource(Order entity) {
		return new OrderResource(entity.getUser(),
				new BillingInfo(entity.getCardName(), entity.getCardNumber(), entity.getDates()), entity.getProducts(),
				entity.getStatus(), entity.getAdditionalInfo());
	}

	@Override
	public OrderResource toResource(Order entity) {
		return createResourceWithId(entity.getId(), entity);
	}

}