package poc.ecommerce.api.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import poc.ecommerce.model.response.ResponseHTTP;
import poc.ecommerce.model.response.ResponseHTTPErrorEnum;

/**
 * Util class to create http responses
 * 
 * @author Agustín
 *
 */
public class ResponseUtil {
	/**
	 * create the response for UNAUTHORIZED users
	 * 
	 * @return ResponseEntity
	 */
	public static ResponseEntity<?> createResponseUnauthorized() {
		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.UNAUTHORIZED.value());
		responseHTTP.setMessage(ResponseHTTPErrorEnum.USER_UNAUTHORIZED.toString());
		responseHTTP.setError(ResponseHTTPErrorEnum.USER_UNAUTHORIZED.toString());
		ResponseEntity<?> response = new ResponseEntity<>(responseHTTP, HttpStatus.UNAUTHORIZED);
		return response;
	}

	/**
	 * create the response for NOT_FOUND objects
	 * 
	 * @return ResponseEntity
	 */
	public static ResponseEntity<?> createResponseNotFound(Exception e) {
		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.NOT_FOUND.value());
		responseHTTP.setMessage(e.getMessage());
		responseHTTP.setError(e.getMessage());
		ResponseEntity<?> response = new ResponseEntity<>(responseHTTP, HttpStatus.NOT_FOUND);
		return response;
	}
	
	/**
	 * create the response for EXPECTATION_FAILED objects
	 * 
	 * @return ResponseEntity
	 */
	public static ResponseEntity<?> createResponseExpectationFailed(Exception e) {
		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.EXPECTATION_FAILED.value());
		responseHTTP.setMessage(e.getMessage());
		responseHTTP.setError(e.getMessage());
		ResponseEntity<?> response = new ResponseEntity<>(responseHTTP, HttpStatus.EXPECTATION_FAILED);
		return response;
	}
}
