package poc.ecommerce.api.resource;
import org.springframework.hateoas.ResourceSupport;

/**
 * Spring HATEOAS-oriented DTO for {@see Product} entity
 *
 * @author dnardelli
 */
public class ProductResource extends ResourceSupport {

    private final String name;
    private final double price;

    public ProductResource(String name, String currency, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

}