package poc.ecommerce.api.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import poc.ecommerce.exception.NotFoundException;

@ControllerAdvice
public class ExceptionResolver {

	@ExceptionHandler(value = NotFoundException.class)
	public void handleNotFoundException(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	public void handleIllegalArgumentException(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
