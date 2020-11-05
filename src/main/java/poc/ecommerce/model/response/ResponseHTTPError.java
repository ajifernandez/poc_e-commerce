package poc.ecommerce.model.response;

/**
 * Class which represent a response http error.
 */
public class ResponseHTTPError {

	/** Code. */
	private String code;

	/** Object name. */
	private String objectName;

	/** Field. */
	private String field;

	/** Default message. */
	private String defaultMessage;

	/**
	 * Constructor.
	 */
	public ResponseHTTPError() {
		super();
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the defaultMessage
	 */
	public String getDefaultMessage() {
		return defaultMessage;
	}

	/**
	 * @param defaultMessage the defaultMessage to set
	 */
	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

}