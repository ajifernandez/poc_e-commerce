package poc.ecommerce.api.convert;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import poc.ecommerce.api.ProductController;
import poc.ecommerce.api.resource.ProductResource;
import poc.ecommerce.model.Product;

@Component
public class ProductResourceAssembler extends ResourceAssemblerSupport<Product, ProductResource> {


    public ProductResourceAssembler() {
        super(ProductController.class, ProductResource.class);
    }

    @Override
    protected ProductResource instantiateResource(Product entity) {
        return new ProductResource(
            entity.getName(),
            Product.CURRENCY,
            entity.getPrice()
        );
    }

    @Override
    public ProductResource toResource(Product entity) {
        return createResourceWithId(entity.getId(), entity);
    }

}