package poc.ecommerce;

import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.Assert;
//import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import poc.ecommerce.repository.OrderRepository;
import poc.ecommerce.repository.ProductRepository;
import poc.ecommerce.repository.ShoppingCartRepository;

@ActiveProfiles("test")
@SpringBootTest(classes = WebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class SpringContextTest {

	private static final String USER_FORM = "userForm";

	/**
	 * Controllers
	 */
	@Autowired
	private UserController userController;
	@Autowired
	private ProductController productController;
	@Autowired
	private ShoppingCartController shoppingCartController;
	@Autowired
	private OrderController orderController;

	/**
	 * Repositorys
	 */
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	private static boolean launched;

	@AfterEach
	public void clearData() {
		orderRepository.deleteAll();
//		shoppingCartRepository.clearAll();
		productRepository.deleteAll();
	}

	@BeforeEach
	public void insertUsersData() {
		if (!launched) {
			BeanPropertyBindingResult bindingResult1 = new BeanPropertyBindingResult(getNormalUser(1), USER_FORM);
			Assert.assertEquals("redirect:/welcome", userController.registration(getNormalUser(1), bindingResult1));

			BeanPropertyBindingResult bindingResult2 = new BeanPropertyBindingResult(getNormalUser(2), USER_FORM);
			Assert.assertEquals("redirect:/welcome", userController.registration(getNormalUser(2), bindingResult2));

			BeanPropertyBindingResult bindingResult3 = new BeanPropertyBindingResult(getAdminUser(), USER_FORM);
			Assert.assertEquals("redirect:/welcome", userController.registration(getAdminUser(), bindingResult3));

			launched = true;
		}
	}

	@BeforeEach
	public void prepareData() {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getAdminUser(), new BeanPropertyBindingResult(getAdminUser(), USER_FORM)));
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

	}

	private User getNormalUser(Integer i) {
		User user = new User();
		user.setPassword("usuario" + i);
		user.setPasswordConfirm("usuario" + i);
		user.setUsername("usuario" + i);
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
				userController.login(getNormalUser(1), new BeanPropertyBindingResult(getNormalUser(1), USER_FORM)));
		Assert.assertEquals("redirect:/login", userController.logout(getNormalUser(1), null));
	}

	@Test
	public void testLoginValidation() throws Exception {
		assertThrows(BadCredentialsException.class, () -> {
			User adminUser = getNormalUser(1);
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
				userController.login(getNormalUser(1), new BeanPropertyBindingResult(getNormalUser(1), USER_FORM)));
		ResponseEntity<?> retrieveAllProducts = productController.retrieveAllProducts();
		Assert.assertEquals(HttpStatus.SC_OK, retrieveAllProducts.getStatusCodeValue());
		Assert.assertEquals(4, ((List<ProductDto>) ((ResponseHTTP) retrieveAllProducts.getBody()).getValue()).size());
	}

	@Test
	public void testGetProductById() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(1), new BeanPropertyBindingResult(getNormalUser(1), USER_FORM)));
		ResponseEntity<?> retrieveAllProducts = productController.retrieveAllProducts();
		List<ProductDto> productList = ((List<ProductDto>) ((ResponseHTTP) retrieveAllProducts.getBody()).getValue());

		ResponseEntity<?> retrieveProduct = productController.retrieveProduct(productList.get(0).getId());
		Assert.assertEquals(HttpStatus.SC_OK, retrieveProduct.getStatusCodeValue());
		Assert.assertEquals(10.5, ((Product) ((ResponseHTTP) retrieveProduct.getBody()).getValue()).getPrice(), 0);
	}

	@Test
	public void testUpdateProduct() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getAdminUser(), new BeanPropertyBindingResult(getAdminUser(), USER_FORM)));
		ResponseEntity<?> retrieveAllProducts = productController.retrieveAllProducts();
		List<ProductDto> productList = ((List<ProductDto>) ((ResponseHTTP) retrieveAllProducts.getBody()).getValue());

		ResponseEntity<?> retrieveProduct = productController.retrieveProduct(productList.get(0).getId());
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
		ResponseEntity<?> retrieveAllProducts = productController.retrieveAllProducts();
		List<ProductDto> productList = ((List<ProductDto>) ((ResponseHTTP) retrieveAllProducts.getBody()).getValue());

		ResponseEntity<?> retrieveProduct = productController.retrieveProduct(1L);
		Product product = ((Product) ((ResponseHTTP) retrieveProduct.getBody()).getValue());
		ProductDto pDto = new ProductDto();
		pDto.setName("name updated");
		pDto.setPrice(productList.get(0).getPrice());
		ResponseEntity<?> updateProduct = productController.updateProduct(-1L, pDto);
		Assert.assertEquals(HttpStatus.SC_NOT_FOUND, updateProduct.getStatusCodeValue());
	}

	@Test
	public void testGetProductByIdNotFound() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(1), new BeanPropertyBindingResult(getNormalUser(1), USER_FORM)));
		ResponseEntity<?> retrieveProduct = productController.retrieveProduct(-1L);
		Assert.assertEquals(HttpStatus.SC_NOT_FOUND, retrieveProduct.getStatusCodeValue());
	}

	@Test
	public void testDeleteProduct() throws Exception {
		Assert.assertEquals("redirect:/welcome",
				userController.login(getAdminUser(), new BeanPropertyBindingResult(getAdminUser(), USER_FORM)));
		ResponseEntity<?> retrieveAllProducts = productController.retrieveAllProducts();
		List<ProductDto> productList = ((List<ProductDto>) ((ResponseHTTP) retrieveAllProducts.getBody()).getValue());
		ResponseEntity<?> retrieveProduct = productController.deleteProduct(productList.get(0).getId());
		Assert.assertEquals(HttpStatus.SC_NO_CONTENT, retrieveProduct.getStatusCodeValue());
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
				userController.login(getNormalUser(1), new BeanPropertyBindingResult(getNormalUser(1), USER_FORM)));
		ResponseEntity<?> findProducts = productController.retrieveProductByName("duc", null);
		Assert.assertEquals(HttpStatus.SC_OK, findProducts.getStatusCodeValue());
		Assert.assertNotNull(((List<Product>) ((ResponseHTTP) findProducts.getBody()).getValue()));

		findProducts = productController.retrieveProductByName("duc", 11.0);
		Assert.assertEquals(HttpStatus.SC_OK, findProducts.getStatusCodeValue());
		Assert.assertNotNull(((List<Product>) ((ResponseHTTP) findProducts.getBody()).getValue()));
	}

	@Test
	public void testOrderNotUser() {
		// Login as normal user
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(1), new BeanPropertyBindingResult(getNormalUser(1), USER_FORM)));
		// Find and select products
		ResponseEntity<?> retrieveAllProducts = productController.retrieveAllProducts();
		ResponseEntity<?> retrieveShoppingCart = shoppingCartController
				.retrieveShoppingCart(getNormalUser(1).getUsername());

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
		orderDto.setUser(getNormalUser(1));

		orderController.createOrder(orderDto);
		ResponseEntity<?> retrieveOrders = orderController.retrieveOrders(getNormalUser(1).getUsername());
		List<OrderDto> order = (List<OrderDto>) ((ResponseHTTP) retrieveOrders.getBody()).getValue();
		userController.logout(getNormalUser(1), null);

		userController.login(getNormalUser(2), new BeanPropertyBindingResult(getNormalUser(2), USER_FORM));

		assertThrows(AccessDeniedException.class, () -> {
			// do whatever you want to do here
			ResponseEntity<?> checkoutOrder = orderController.checkoutOrder(order.get(0).getId());
		});

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFullProcess() throws Exception {
		// Login as normal user
		Assert.assertEquals("redirect:/welcome",
				userController.login(getNormalUser(1), new BeanPropertyBindingResult(getNormalUser(1), USER_FORM)));
		// Find and select products
		ResponseEntity<?> retrieveAllProducts = productController.retrieveAllProducts();
		ResponseEntity<?> retrieveShoppingCart = shoppingCartController
				.retrieveShoppingCart(getNormalUser(1).getUsername());

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
		orderDto.setUser(getNormalUser(1));

		orderController.createOrder(orderDto);

		Assert.assertEquals("redirect:/login", userController.logout(getNormalUser(1), null));

		// Login as admin user
		Assert.assertEquals("redirect:/welcome",
				userController.login(getAdminUser(), new BeanPropertyBindingResult(getAdminUser(), USER_FORM)));
		retrieveShoppingCart = shoppingCartController.retrieveShoppingCart(getNormalUser(1).getUsername());
		shoppingCartDto = (ShoppingCartDto) ((ResponseHTTP) retrieveShoppingCart.getBody()).getValue();
//		orderController.retrieveAllOrdersByUsername(getNormalUser(1).getUsername());
		ResponseEntity<?> retrieveAllOrders = orderController.retrieveAllOrders();
		List<OrderDto> orders = (List<OrderDto>) ((ResponseHTTP) retrieveAllOrders.getBody()).getValue();
		orderController.checkoutOrder(orders.get(0).getId());

		ResponseEntity<?> retrieveOrder = orderController.retrieveOrder(orders.get(0).getId());
		OrderDto order = (OrderDto) ((ResponseHTTP) retrieveOrder.getBody()).getValue();
		Assert.assertEquals("PROCESSED", order.getStatus());
	}

}