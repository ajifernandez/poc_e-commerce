package poc.ecommerce.service;

public interface SecurityService {
	/**
	 * Find the username that is logged
	 * 
	 * @return the username
	 */
	String findLoggedInUsername();

	/**
	 * login into the application
	 * 
	 * @param username username
	 * @param password password
	 */
	void autoLogin(String username, String password);

	/**
	 * Check if the logged user has the permission
	 * 
	 * @param permission
	 * @return has the permission or not
	 */
	boolean checkPermissions(String permission);
}
