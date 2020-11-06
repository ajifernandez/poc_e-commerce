package poc.ecommerce;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for SpringFox (swagger)
 * 
 * @author Agustín
 *
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {
	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("poc.ecommerce")).paths(PathSelectors.any()).build()
				.apiInfo(getApiInfo());
	}

	private ApiInfo getApiInfo() {
		return new ApiInfo("E-commerce", "E-Commerce", "0.0.1-SNAPSHOT",
				"https://www.apache.org/licenses/LICENSE-2.0.txt",
				new Contact("http://mhp-net.es/", "http://mhp-net.es/", "agustinjf87@gmail.com"),
				"APACHE LICENSE, VERSION 2.0", "https://www.apache.org/licenses/LICENSE-2.0.txt",
				Collections.emptyList());
	}
}