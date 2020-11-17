package poc.ecommerce;

import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BeanPropertyBindingResult;

import poc.ecommerce.api.OrderController;
import poc.ecommerce.api.ProductController;
import poc.ecommerce.api.ShoppingCartController;
import poc.ecommerce.api.UserController;
import poc.ecommerce.api.model.BillingInfoDto;
import poc.ecommerce.api.model.OrderDto;
import poc.ecommerce.api.model.ProductDto;
import poc.ecommerce.api.model.ProductsInProcessDto;
import poc.ecommerce.api.model.ShoppingCartDto;
import poc.ecommerce.model.Product;
import poc.ecommerce.model.Role;
import poc.ecommerce.model.User;
import poc.ecommerce.model.response.ResponseHTTP;

@ActiveProfiles("test")
@SpringBootTest(classes = WebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class SpringContextTest {

	private static final String USER_FORM = "userForm";

	@Autowired
	private UserController userController;
	@Autowired
	private ProductController productController;
	@Autowired
	private ShoppingCartController shoppingCartController;
	@Autowired
	private OrderController orderController;

	private static boolean launched;

	@BeforeEach
	public void prepareData() {
		if (!launched) {
			BeanPropertyBindingResult bindingResult1 = new BeanPropertyBindingResult(getNormalUser(), USER_FORM);
			Assert.assertEquals("redirect:/welcome", userController.registration(getNormalUser(), bindingResult1));

			BeanPropertyBindingResult bindingResult3 = new BeanPropertyBindingResult(getAdminUser(), USER_FORM);
			Assert.assertEquals("redirect:/welcome", userController.registration(getAdminUser(), bindingResult3));

			ProductDto product = new ProductDto();
			product.setName("Product 1");
			product.setPrice(10.5);
			Assert.assertEquals(HttpStatus.SC_CREATED, productController.createProduct(product).getStatusCodeValue());

			product = new ProductDto();
			product.setName("Product 2");
			product.setPrice(12.5);
			Assert.assertEquals(HttpStatus.SC_CREATED, productController.createProduct(product).getStatusCodeValue());

			product = new ProductDto();
			product.setName("Product 3");
			product.setPrice(13.5);
			Assert.assertEquals(HttpStatus.SC_CREATED, productController.createProduct(product).getStatusCodeValue());

			product = new ProductDto();
			product.setName("Product 4");
			product.setPrice(14.5);
			Assert.assertEquals(HttpStatus.SC_CREATED, productController.createProduct(product).getStatusCodeValue());

			Assert.assertEquals("redirect:/login", userController.logout(getAdminUser(), null));

			launched = true;
		}

	}

	private User getNormalUser() {
		User user = new User();
		user.setPassword("usuario1");
		user.setPasswordConfirm("usuario1");
		user.setUsername("usuario1");
		user.setRole(Role.ROLE_USER.name());
		return user;
	}

	private User getAdminUser() {
		User user = new User();
		user.setPassword("administrator");
		user.setPasswordConfirm("administrator");
		user.setUsername("administrator");
		user.setRole(Role.ROLE_ADMIN.name());
		return user;
	}

	@Test
	public void testRegisterValidationShortUserNamePassword() throws Exception {
		User user = new User();
		user.setPassword("short");
		user.setPasswordConfirm("short");
		user.setUsername("short");
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(user, USER_FORM);
		String registration = userController.registration(user, bindingResult);
		Assert.assertEquals("registration", registration);
	}

	@Test
	public void testRegisterValidationLongUserNamePassword() throws Exception {
		User user = new User();
		user.setPassword("0123456789012345678901234567890123456789");
		user.setPasswordConfirm("0123456789012345678901234567890123456789");
		user.setUsername("0123456789012345678901234567890123456789");
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(user, USER_FORM);
		String registration = userController.registration(user, bindingResult);
		Assert.assertEquals("registration", registration);
	}

	@Test
	public void testRegisterValidationPasswords() throws Exception {
		User user = new User();
		user.setPassword("1234567890");
		user.setPasswordConfirm("123456789");
		user.setUsername("administrator");
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(user, USER_FORM);
		String registration = userController.registration(user, bindingResult);
		Assert.assertEquals("registration", registration);
	}

	@Test
	public void testRegisterValidationUserExists() throws Exception {
		User user = new User();
		user.setPassword("1234567890");
		user.setPasswordConfirm("1234567");
		user.setUsername("short");
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(user, USER_FORM);
		String registration = userController.registration(user, bindingResult);
		Assert.assertEquals("registration", registration);
	}

	@Test
	public void testLogin() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(), new BeanPropertyBindingResult(getNormalUser(), USER_FORM)));
		Assert.assertEquals("redirect:/login", userController.logout(getNormalUser(), null));
	}

	@Test
	public void testLoginValidation() throws Exception {
		assertThrows(BadCredentialsException.class, () -> {
			User adminUser = getNormalUser();
			adminUser.setPassword("0123");
			Assert.assertEquals("login",
					userController.login(adminUser, new BeanPropertyBindingResult(adminUser, USER_FORM)));
		});
	}

	@Test
	public void testListProductsNotLogged() throws Exception {
		Assert.assertEquals("redirect:/login", userController.logout(getAdminUser(), null));
		assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
			productController.retrieveAllProducts();
		});
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListProducts() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(), new BeanPropertyBindingResult(getNormalUser(), USER_FORM)));
		ResponseEntity<?> retrieveAllProducts = productController.retrieveAllProducts();
		Assert.assertEquals(HttpStatus.SC_OK, retrieveAllProducts.getStatusCodeValue());
		Assert.assertEquals(4, ((List<ProductDto>) ((ResponseHTTP) retrieveAllProducts.getBody()).getValue()).size());
	}

	@Test
	public void testGetProductById() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(), new BeanPropertyBindingResult(getNormalUser(), USER_FORM)));
		ResponseEntity<?> retrieveProduct = productController.retrieveProduct(1L);
		Assert.assertEquals(HttpStatus.SC_OK, retrieveProduct.getStatusCodeValue());
		Assert.assertEquals(10.5, ((Product) ((ResponseHTTP) retrieveProduct.getBody()).getValue()).getPrice(), 0);
	}

	@Test
	public void testUpdateProduct() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getAdminUser(), new BeanPropertyBindingResult(getAdminUser(), USER_FORM)));
		ResponseEntity<?> retrieveProduct = productController.retrieveProduct(1L);
		Product product = ((Product) ((ResponseHTTP) retrieveProduct.getBody()).getValue());
		ProductDto pDto = new ProductDto();
		pDto.setName("name updated");
		pDto.setPrice(product.getPrice());
		ResponseEntity<?> updateProduct = productController.updateProduct(product.getId(), pDto);
		Assert.assertEquals(HttpStatus.SC_OK, updateProduct.getStatusCodeValue());
		Assert.assertEquals("name updated", ((Product) ((ResponseHTTP) updateProduct.getBody()).getValue()).getName());
	}

	@Test
	public void testUpdateProductNotFound() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getAdminUser(), new BeanPropertyBindingResult(getAdminUser(), USER_FORM)));
		ResponseEntity<?> retrieveProduct = productController.retrieveProduct(1L);
		Product product = ((Product) ((ResponseHTTP) retrieveProduct.getBody()).getValue());
		ProductDto pDto = new ProductDto();
		pDto.setName("name updated");
		pDto.setPrice(product.getPrice());
		ResponseEntity<?> updateProduct = productController.updateProduct(-1L, pDto);
		Assert.assertEquals(HttpStatus.SC_NOT_FOUND, updateProduct.getStatusCodeValue());
	}

	@Test
	public void testGetProductByIdNotFound() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(), new BeanPropertyBindingResult(getNormalUser(), USER_FORM)));
		ResponseEntity<?> retrieveProduct = productController.retrieveProduct(-1L);
		Assert.assertEquals(HttpStatus.SC_NOT_FOUND, retrieveProduct.getStatusCodeValue());
	}

	@Test
	public void testDeleteProduct() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getAdminUser(), new BeanPropertyBindingResult(getAdminUser(), USER_FORM)));
		ResponseEntity<?> retrieveProduct = productController.deleteProduct(1L);
		Assert.assertEquals(HttpStatus.SC_EXPECTATION_FAILED, retrieveProduct.getStatusCodeValue());
	}

	@Test
	public void testDeleteProductNotFound() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getAdminUser(), new BeanPropertyBindingResult(getAdminUser(), USER_FORM)));
		ResponseEntity<?> deleteProduct = productController.deleteProduct(-1L);
		Assert.assertEquals(HttpStatus.SC_NOT_FOUND, deleteProduct.getStatusCodeValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFilterProducts() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(), new BeanPropertyBindingResult(getNormalUser(), USER_FORM)));
		ResponseEntity<?> findProducts = productController.retrieveProductByName("duc", null);
		Assert.assertEquals(HttpStatus.SC_OK, findProducts.getStatusCodeValue());
		Assert.assertNotNull(((List<Product>) ((ResponseHTTP) findProducts.getBody()).getValue()));

		findProducts = productController.retrieveProductByName("duc", 11.0);
		Assert.assertEquals(HttpStatus.SC_OK, findProducts.getStatusCodeValue());
		Assert.assertNotNull(((List<Product>) ((ResponseHTTP) findProducts.getBody()).getValue()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFullProcess() throws Exception {
		// Login as normal user
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(), new BeanPropertyBindingResult(getNormalUser(), USER_FORM)));
		// Find and select products
		ResponseEntity<?> retrieveAllProducts = productController.retrieveAllProducts();
		ResponseEntity<?> retrieveShoppingCart = shoppingCartController
				.retrieveShoppingCart(getNormalUser().getUsername());

		ShoppingCartDto shoppingCartDto = (ShoppingCartDto) ((ResponseHTTP) retrieveShoppingCart.getBody()).getValue();
		List<ProductsInProcessDto> products = new ArrayList<ProductsInProcessDto>();

		List<ProductDto> productsList = (List<ProductDto>) ((ResponseHTTP) retrieveAllProducts.getBody()).getValue();
		ProductsInProcessDto e = new ProductsInProcessDto();
		e.setProduct(productsList.get(0));
		e.setAmount(1);
		products.add(e);
		e = new ProductsInProcessDto();
		e.setProduct(productsList.get(1));
		e.setAmount(5);
		products.add(e);
		shoppingCartDto.setProducts(products);
		// Update shoppingCart
		shoppingCartController.updateShoppingCart(shoppingCartDto);

		OrderDto orderDto = new OrderDto();
		BillingInfoDto billingInfo = new BillingInfoDto();
		billingInfo.setCardName("CARD NAME");
		billingInfo.setCardNumber("CARD NUMBER");
		billingInfo.setDates("DATES");
		orderDto.setBillingInfo(billingInfo);
		orderDto.setShoppingcartId(shoppingCartDto.getId());
		orderDto.setUser(getNormalUser());

		orderController.createOrder(orderDto);

		Assert.assertEquals("redirect:/login", userController.logout(getNormalUser(), null));
		
		// Login as admin user
		Assert.assertEquals("redirect:/welcome",
				userController.login(getAdminUser(), new BeanPropertyBindingResult(getAdminUser(), USER_FORM)));
		retrieveShoppingCart = shoppingCartController.retrieveShoppingCart(getNormalUser().getUsername());
		shoppingCartDto = (ShoppingCartDto) ((ResponseHTTP) retrieveShoppingCart.getBody()).getValue();
//		orderController.retrieveAllOrdersByUsername(getNormalUser().getUsername());
		ResponseEntity<?> retrieveAllOrders = orderController.retrieveAllOrders();
		List<OrderDto> orders = (List<OrderDto>) ((ResponseHTTP) retrieveAllOrders.getBody()).getValue();
		orderController.checkoutOrder(orders.get(0).getId());

		ResponseEntity<?> retrieveOrder = orderController.retrieveOrder(orders.get(0).getId());
		OrderDto order = (OrderDto) ((ResponseHTTP) retrieveOrder.getBody()).getValue();
		Assert.assertEquals("PROCESSED", order.getStatus());
	}

}