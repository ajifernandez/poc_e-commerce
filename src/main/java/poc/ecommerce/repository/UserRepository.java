package poc.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poc.ecommerce.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
