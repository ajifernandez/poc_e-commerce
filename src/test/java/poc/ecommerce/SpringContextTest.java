package poc.ecommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import poc.ecommerce.model.BillingInfo;
import poc.ecommerce.model.Order;
import poc.ecommerce.model.Product;
import poc.ecommerce.model.Role;
import poc.ecommerce.model.ShoppingCart;
import poc.ecommerce.model.User;
import poc.ecommerce.service.OrderService;
import poc.ecommerce.service.ProductService;
import poc.ecommerce.service.ShoppingCartService;
import poc.ecommerce.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
public class SpringContextTest {

	private static final String CURRENCY = "EUR";
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private OrderService orderService;

	/**
	 * Users
	 */
	private User user1;
	private User user2;
	private User user3; // administrator

	@Before
	public void prepareData() {
		user1 = new User();
		user1.setPassword("usuario1");
		user1.setUsername("usuario1");
		user1.setRole(Role.ROLE_USER.name());
		userService.save(user1);

		user2 = new User();
		user2.setPassword("usuario2");
		user2.setUsername("usuario2");
		user2.setRole(Role.ROLE_USER.name());
		userService.save(user1);

		user3 = new User();
		user3.setPassword("administrator");
		user3.setUsername("administrator");
		user3.setRole(Role.ROLE_ADMIN.name());
	}

	@Test
	public void testAll() {
		testProducts();
		testShoppingCart();
		testOrderCheckout();
	}

	private void testProducts() {
		List<Product> allProducts = productService.getAllProducts();
		assertEquals(allProducts.size(), 0);
		productService.createProduct("product1", CURRENCY, 10.5);
		productService.createProduct("product2", CURRENCY, 11.5);
		productService.createProduct("product3", CURRENCY, 12.5);
		Product product = productService.createProduct("product4", CURRENCY, 13.5);
		allProducts = productService.getAllProducts();
		assertEquals(allProducts.size(), 4);

		Optional<Product> productById = productService.getProductById(product.getId());
		assertEquals(product.getId(), productById.get().getId());

		productService.deleteProduct(product);
		allProducts = productService.getAllProducts();
		assertEquals(allProducts.size(), 3);

		allProducts = productService.getFilterProduct("duc", null);
		assertEquals(allProducts.size(), 3);
		allProducts = productService.getFilterProduct("duc", 11.0);
		assertEquals(allProducts.size(), 1);
	}

	private void testShoppingCart() {
		List<Product> allProducts = productService.getAllProducts();

		List<Product> products = new ArrayList<>();
		products.add(allProducts.get(0));
		products.add(allProducts.get(1));
		ShoppingCart createShoppingCart1 = shoppingCartService.createShoppingCart(user1, products);
		assertNotNull(createShoppingCart1);

		ShoppingCart shoppingCartById = shoppingCartService.getShoppingCartById(createShoppingCart1.getId()).get();
		assertNotNull(shoppingCartById);

		products = new ArrayList<>();
		products.add(allProducts.get(0));
		products.add(allProducts.get(1));
		products.add(allProducts.get(2));
		shoppingCartById.setProducts(products);
		shoppingCartService.updateShoppingCart(shoppingCartById);
		assertEquals(shoppingCartService.getShoppingCartById(createShoppingCart1.getId()).get().getProducts().size(),
				3);

		assertEquals(shoppingCartService.getAllShoppingCarts().size(), 1);
	}

	private void testOrderCheckout() {
		BillingInfo billingInfo = new BillingInfo();
		billingInfo.setCardName("USUARIO1");
		billingInfo.setCardNumber("4539585460951520");
		billingInfo.setDates("08/2020");
		Order createOrder = orderService.createOrder(user1, 1L, billingInfo);
		assertEquals(createOrder.getStatus(), "CREATED");

		createOrder.setStatus("PROCESSED");
		orderService.updateOrder(createOrder);

		Optional<Order> orderById = orderService.getOrderById(createOrder.getId());
		assertEquals(orderById.get().getStatus(), "PROCESSED");
	}

}