package com.ashishpinninti.myretail.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ashishpinninti.myretail.TestConfig;
import com.ashishpinninti.myretail.entity.CurrentPrice;
import com.ashishpinninti.myretail.entity.Product;
import com.ashishpinninti.myretail.exception.ExternalAPICallFailedException;
import com.ashishpinninti.myretail.exception.ProductAlreadyExistException;
import com.ashishpinninti.myretail.exception.ProductNotFoundException;
import com.ashishpinninti.myretail.repository.ProductRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService = new ProductServiceImpl();

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
	public void getProduct() {
		String productId = product.getId().toString();
		when(productRepository.findOne(productId)).thenReturn(product);
		Product actual = productService.getProduct(productId);
		assertSame(product, actual);
	}

	@Test(expected = ProductNotFoundException.class)
	public void getProductNotFound() {
		String productId = product.getId().toString();
		when(productRepository.findOne(productId)).thenReturn(null);
		productService.getProduct(productId);
	}
	
	@Test(expected = ExternalAPICallFailedException.class)
	public void getProductWithIdNotInExternalAPI() {
		String productId = productUpdate.getId().toString();
		when(productRepository.findOne(productId)).thenReturn(productUpdate);
		productService.getProduct(productId);
	}

	@Test
	public void getAllProductsNoPaging() {
		List<Product> productList = Arrays.asList(product, productUpdate);
		when(productRepository.findAll()).thenReturn(productList);

		List<Product> allProducts = productService.getAllProducts(null, null);

		List<BigInteger> productIds = Arrays.asList(product.getId(),
				productUpdate.getId());
		assertThat(productIds.contains(allProducts.get(0).getId())).as(
				"Product should be present in the returned user list").isTrue();
		assertThat(productIds.contains(allProducts.get(1).getId())).as(
				"Product should be present in the returned user list").isTrue();
	}

	@Test
	public void addNewProduct() {
		when(productRepository.insert(newProduct)).thenReturn(newProduct);
		Product actual = productService.addProduct(newProduct);
		assertThat(actual).isNotNull();
		verify(productRepository).insert(newProduct);
	}

	@Test(expected = ProductAlreadyExistException.class)
	public void addAlreadyPresentProduct() {
		String productId = product.getId().toString();
		when(productRepository.findOne(productId)).thenReturn(product);
		productService.addProduct(product);
	}

	@Test
	public void addNewProductWithGivenId() {
		String productId = product.getId().toString();
		when(productRepository.findOne(productId)).thenReturn(null);
		productService.addProduct(product);
		verify(productRepository).insert(product);
	}

//	@Test(expected = ExternalAPICallFailedException.class)
//	public void addProductWithIdNotInExternalAPI() {
//		String productId = productUpdate.getId().toString();
//		when(productRepository.findOne(productId)).thenReturn(null);
//		productService.addProduct(productUpdate);
//	}

	@Test
	public void updateProduct() {
		String productId = product.getId().toString();
		when(productRepository.findOne(productId)).thenReturn(product);
		Product actual = productService.updateProduct(productId, productUpdate);
		assertThat(productUpdate.getCurrent_price()).isEqualTo(
				actual.getCurrent_price());
	}

	@Test(expected = ProductNotFoundException.class)
	public void updateProductNotFound() {
		String productId = product.getId().toString();
		when(productRepository.findOne(productId)).thenReturn(null);
		productService.updateProduct(productId, productUpdate);
	}

	@Test
	public void deleteProduct() {
		String productId = product.getId().toString();
		when(productRepository.findOne(productId)).thenReturn(product);
		productService.deleteProduct(productId);
		verify(productRepository).delete(productId);
	}

	@Test(expected = ProductNotFoundException.class)
	public void deleteProductNotFound() {
		String productId = product.getId().toString();
		when(productRepository.findOne(productId)).thenReturn(null);
		productService.deleteProduct(productId);
	}

}
