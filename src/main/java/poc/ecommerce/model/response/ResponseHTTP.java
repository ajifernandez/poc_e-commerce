package poc.ecommerce.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Response HTTP.
 */
public class ResponseHTTP {

	/** Status code value. */
	private int status;

	/** Status code string. */
	private String error;

	/** Errors. */
	private List<ResponseHTTPError> errors;

	/** Message. */
	private String message;

	/** Path. */
	private String path;

	private Object value;

	/**
	 * Constructor.
	 */
	public ResponseHTTP() {
		super();
		this.errors = new ArrayList<>();
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the errors
	 */
	public List<ResponseHTTPError> getErrors() {
		return new ArrayList<>(errors);
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<ResponseHTTPError> errors) {
		this.errors = new ArrayList<>(errors);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

}