package poc.ecommerce.api.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import poc.ecommerce.api.exception.NotFoundException;
import poc.ecommerce.model.response.ResponseHTTP;

public class ResponseUtil {
	/**
	 * create the response for UNAUTHORIZED users
	 * 
	 * @return ResponseEntity
	 */
	public static ResponseEntity<?> createResponseUNAUTHORIZED() {
		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.UNAUTHORIZED.value());
		responseHTTP.setMessage("UNAUTHORIZED");
		responseHTTP.setError("UNAUTHORIZED");
		ResponseEntity<?> response = new ResponseEntity<>(responseHTTP, HttpStatus.UNAUTHORIZED);
		return response;
	}

	/**
	 * create the response for NOT_FOUND objects
	 * 
	 * @return ResponseEntity
	 */
	public static ResponseEntity<?> createResponseNOT_FOUND(NotFoundException e) {
		ResponseHTTP responseHTTP = new ResponseHTTP();
		responseHTTP.setStatus(HttpStatus.NOT_FOUND.value());
		responseHTTP.setMessage(e.getMessage());
		responseHTTP.setError(e.getMessage());
		ResponseEntity<?> response = new ResponseEntity<>(responseHTTP, HttpStatus.NOT_FOUND);
		return response;
	}
}
