package poc.ecommerce.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poc.ecommerce.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Finds a User through the given username.
	 *
	 * @param username the username to look for
	 * @return the User that was found (if any)
	 */
	Optional<User> findByUsername(String username);

}