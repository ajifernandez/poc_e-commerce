package poc.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Main class
 * 
 * @author Agustín
 *
 */
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
public class WebApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebApplication.class, args);
	}
}
