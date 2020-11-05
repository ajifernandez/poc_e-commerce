package poc.ecommerce.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import poc.ecommerce.entity.User;

public interface SecurityService {

	/**
	 * Authenticates a User through their credentials.
	 *
	 * @param username the username to login
	 * @param password the password to validate
	 * @exception AuthenticationException when user credentials are invalid
	 * @return an auth token that can be used for reference
	 */
	String authenticate(final String username, final String password);

	/**
	 * Authenticates a User through an auth token.
	 *
	 * @param token the auth token to validate
	 * @exception BadCredentialsException when auth token is invalid
	 */
	void authenticate(final String token);

	/**
	 * Gets the current logged in User.
	 *
	 * @return the logged in User information
	 */
	User getCurrentUser();

}