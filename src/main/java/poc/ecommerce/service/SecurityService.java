package poc.ecommerce.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);

	boolean checkPermissions(String permission);
}
