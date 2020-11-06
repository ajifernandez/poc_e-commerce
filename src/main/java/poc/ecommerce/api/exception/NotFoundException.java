package poc.ecommerce.api.exception;

/**
 * Excetpion when the entity is not found
 * 
 * @author Agustín
 *
 */
public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException(String entity) {
		super(entity + " not found");
	}

}