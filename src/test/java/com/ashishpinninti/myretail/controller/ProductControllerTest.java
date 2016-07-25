package com.ashishpinninti.myretail.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ashishpinninti.myretail.TestConfig;
import com.ashishpinninti.myretail.entity.CurrentPrice;
import com.ashishpinninti.myretail.entity.Product;
import com.ashishpinninti.myretail.service.ProductService;
import com.google.gson.Gson;

/**
 * Unit Tests for ProductController
 * 
 * @author apinninti
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@WebAppConfiguration
public class ProductControllerTest {

	@Mock
	private ProductService productService;

	@InjectMocks
	ProductController productController = new ProductController();

	private MockMvc mockMvc;

	private Product product, productUpdate, newProduct;

	public void assertSame(Product product1, Product product2) {
		if (product1 == product2) {
			return;
		}
		assertThat(product1).isNotNull();
		assertThat(product2).isNotNull();
		assertThat(product1.getId()).isEqualTo(product2.getId());
		assertThat(product1.getCurrent_price()).isEqualTo(
				product1.getCurrent_price());
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		product = getBasicProduct();
		productUpdate = getBasicProductUpdate();
		newProduct = getBasicNewProduct();
		mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
	}

	public Product getBasicProduct() {
		CurrentPrice current_price = new CurrentPrice("7", "INR");
		Product product = new Product();
		product.setId(new BigInteger("13860428"));
		product.setCurrent_price(current_price);
		return product;
	}

	public Product getBasicProductUpdate() {
		CurrentPrice current_price1 = new CurrentPrice("8", "USD");
		Product productUpdate = new Product();
		productUpdate.setId(new BigInteger("23456532"));
		productUpdate.setCurrent_price(current_price1);
		return productUpdate;
	}

	public Product getBasicNewProduct() {
		CurrentPrice current_price2 = new CurrentPrice("9", "INR");
		Product newProduct = new Product();
		newProduct.setCurrent_price(current_price2);
		return newProduct;
	}

	@Test
	public void getProduct() throws Exception {
		String productId = product.getId().toString();
		when(productService.getProduct(productId)).thenReturn(product);

		mockMvc.perform(get("/products/v1/" + productId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$['id']", is(13860428)))
				.andExpect(jsonPath("$['current_price']['value']", is("7")))
				.andExpect(
						jsonPath("$['current_price']['currency_code']",
								is("INR")));

		verify(productService).getProduct(productId);
	}

	@Test
	public void getAllProductsNoPaging() throws Exception {
		List<Product> productList = Arrays.asList(product, productUpdate);
		when(productService.getAllProducts(null, null)).thenReturn(productList);

		mockMvc.perform(get("/products/v1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2)));

		verify(productService).getAllProducts(null, null);
	}

	@Test
	public void addProduct() throws Exception {
		when(productService.addProduct(any(Product.class))).thenReturn(product);
		Gson gson = new Gson();
		String jsonProduct = gson.toJson(product);

		mockMvc.perform(
				post("/products/v1/").contentType(MediaType.APPLICATION_JSON)
						.content(jsonProduct))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$['id']", is(13860428)))
				.andExpect(jsonPath("$['current_price']['value']", is("7")))
				.andExpect(
						jsonPath("$['current_price']['currency_code']",
								is("INR")));

		verify(productService).addProduct(any(Product.class));
	}

	@Test
	public void updateProduct() throws Exception {
		String productId = productUpdate.getId().toString();
		when(productService.updateProduct(eq(productId), any(Product.class)))
				.thenReturn(productUpdate);
		Gson gson = new Gson();
		String jsonProduct = gson.toJson(productUpdate);

		mockMvc.perform(
				put("/products/v1/" + productId).contentType(
						MediaType.APPLICATION_JSON).content(jsonProduct))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$['id']", is(23456532)))
				.andExpect(jsonPath("$['current_price']['value']", is("8")))
				.andExpect(
						jsonPath("$['current_price']['currency_code']",
								is("USD")));
		verify(productService).updateProduct(eq(productId), any(Product.class));
	}

	@Test
	public void deleteProduct() throws Exception {
		String productId = product.getId().toString();
		when(productService.deleteProduct(productId)).thenReturn(
				"deleted product with id: " + productId);

		mockMvc.perform(delete("/products/v1/" + productId)).andExpect(
				status().isOk());
		verify(productService).deleteProduct(productId);
	}

}
