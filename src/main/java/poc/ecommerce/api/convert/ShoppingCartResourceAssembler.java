package poc.ecommerce.api.convert;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import poc.ecommerce.api.ShoppingCartController;
import poc.ecommerce.api.resource.ShoppingCartResource;
import poc.ecommerce.model.ShoppingCart;

@Component
public class ShoppingCartResourceAssembler extends ResourceAssemblerSupport<ShoppingCart, ShoppingCartResource> {

	public ShoppingCartResourceAssembler() {
		super(ShoppingCartController.class, ShoppingCartResource.class);
	}

	@Override
	protected ShoppingCartResource instantiateResource(ShoppingCart entity) {
		return new ShoppingCartResource(entity.getUser(), entity.getProducts());
	}

	@Override
	public ShoppingCartResource toResource(ShoppingCart entity) {
		return createResourceWithId(entity.getId(), entity);
	}

}