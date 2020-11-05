package poc.ecommerce.service;

import poc.ecommerce.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
