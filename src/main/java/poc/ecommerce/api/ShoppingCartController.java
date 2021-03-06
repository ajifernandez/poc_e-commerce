package poc.ecommerce.api;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import poc.ecommerce.api.exception.NotFoundException;
import poc.ecommerce.api.model.ShoppingCartDto;
import poc.ecommerce.api.util.ResponseUtil;
import poc.ecommerce.model.Role;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.response.ResponseHTTP;
import poc.ecommerce.service.SecurityService;
import poc.ecommerce.service.ShoppingCartService;

/**
 * Controller to shopping cart
 * 
 * @author Agustín
 *
 */
@RestController
@PreAuthorize("isFullyAuthenticated()")
@RequestMapping(path = "/shoppingcart")
public class ShoppingCartController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private SecurityService securityService;

	@RequestMapping(path = "/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveShoppingCart(@PathVariable String username) {
		LOGGER.info("Request - Get the shoppingcart by username" + username);
		ResponseEntity<?> response = null;
		try {
			final ShoppingCart shoppingcart = shoppingCartService.getShoppingCartByUsername(username);
			ModelMapper modelMapper = new ModelMapper();
			ShoppingCartDto shoppingcartDTO = modelMapper.map(shoppingcart, ShoppingCartDto.class);

			ResponseHTTP responseHTTP = new ResponseHTTP();
			responseHTTP.setStatus(HttpStatus.OK.value());
			responseHTTP.setValue(shoppingcartDTO);
			response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNotFound(e);
		}
		return response;
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> retrieveShoppingCart(@PathVariable Long id) {
		LOGGER.info("Request - Get the shoppingcart by Id" + id);
		ResponseEntity<?> response = null;
		try {
			final ShoppingCart shoppingcart = shoppingCartService.getShoppingCartById(id)
					.orElseThrow(() -> new NotFoundException("shoppingcart"));
			if (securityService.checkPermissions(Role.ROLE_ADMIN.name())
					|| securityService.findLoggedInUsername().equals(shoppingcart.getUser().getUsername())) {
				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(shoppingcart);
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} else {
				response = ResponseUtil.createResponseUnauthorized();
			}
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNotFound(e);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> updateShoppingCart(@RequestBody @Valid ShoppingCartDto shoppingCartDto) {
		LOGGER.info("Request - Updating the shoppingCart" + shoppingCartDto);
		ResponseEntity<?> response;
		try {
			ShoppingCart shoppingcart = shoppingCartService.getShoppingCartById(shoppingCartDto.getId())
					.orElseThrow(() -> new NotFoundException("product"));
			if (securityService.checkPermissions(Role.ROLE_ADMIN.name())
					|| securityService.findLoggedInUsername().equals(shoppingcart.getUser().getUsername())) {
				ModelMapper modelMapper = new ModelMapper();
				shoppingcart = modelMapper.map(shoppingCartDto, ShoppingCart.class);
				shoppingCartService.updateShoppingCart(shoppingcart);

				ResponseHTTP responseHTTP = new ResponseHTTP();
				responseHTTP.setStatus(HttpStatus.OK.value());
				responseHTTP.setValue(shoppingcart);
				response = new ResponseEntity<>(responseHTTP, HttpStatus.OK);
			} else {
				response = ResponseUtil.createResponseUnauthorized();
			}
		} catch (NotFoundException e) {
			response = ResponseUtil.createResponseNotFound(e);
		}
		return response;
	}

}