package com.ashishpinninti.myretail;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ashishpinninti.myretail.entity.CurrentPrice;
import com.ashishpinninti.myretail.entity.Product;
import com.ashishpinninti.myretail.exception.ExternalAPICallFailedException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class MyretailApplicationTests {

	public Product getBasicProduct() {
		CurrentPrice current_price = new CurrentPrice("7", "INR");
		Product product = new Product();
		product.setId(new BigInteger("13860428"));
		product.setCurrent_price(current_price);
		return product;
	}

	public Product getProductNotInExternalAPI() {
		CurrentPrice current_price1 = new CurrentPrice("8", "USD");
		Product productUpdate = new Product();
		productUpdate.setId(new BigInteger("23456532"));
		productUpdate.setCurrent_price(current_price1);
		return productUpdate;
	}

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

	public String getProductNameFromTargetAPI(String productId) {
		String productName = null;
		try {
			Client client = ClientBuilder.newClient();
			WebTarget resource = client
					.target("https://api.target.com/products/v3/"
							+ productId
							+ "?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz");
			Builder request = resource.request();
			request.accept(MediaType.APPLICATION_JSON);
			// Inject
			JsonParser jsonParser = new JsonParser();
			JsonObject getResponse = jsonParser
					.parse(request.get(String.class)).getAsJsonObject();
			JsonObject productCompositeResponse = getResponse
					.getAsJsonObject("product_composite_response");
			JsonArray itemsArray = productCompositeResponse
					.getAsJsonArray("items");
			productName = itemsArray.get(0).getAsJsonObject()
					.getAsJsonObject("online_description").get("value")
					.getAsString();
		} catch (Exception e) {
			throw new ExternalAPICallFailedException(
					"Call to external API failed for id=" + productId
							+ " not found!");
		}
		return productName;
	}

	@Test
	public void getProduct() {

		Client client = ClientBuilder.newClient();
		WebTarget resource = client
				.target("http://localhost:8080/products/v1/");
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		Product postedProduct = getBasicProduct();
		Gson gson = new Gson();
		String jsonPostedProduct = gson.toJson(postedProduct);
		// Post the product
		Response response = request.post(
				Entity.entity(jsonPostedProduct, MediaType.APPLICATION_JSON),
				Response.class);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		resource = client.target("http://localhost:8080/products/v1/13860428");
		request = resource.request();
		// Get the product
		response = request.get();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		String getJsonProduct = response.readEntity(String.class);
		Product getProduct = gson.fromJson(getJsonProduct, Product.class);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertSame(getProduct, postedProduct);
		assertThat(getProductNameFromTargetAPI("13860428")).isEqualTo(
				getProduct.getName());

		resource = client.target("http://localhost:8080/products/v1/13860428");
		request = resource.request();
		// Delete the product
		response = request.delete();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	}

	@Test
	public void getProductNotFound() {

		Client client = ClientBuilder.newClient();
		WebTarget resource = client
				.target("http://localhost:8080/products/v1/13860428");
		Builder request = resource.request();
		// Get the product
		Response response = request.get();
		assertThat(response.getStatus())
				.isEqualTo(HttpStatus.NOT_FOUND.value());

	}

	@Test
	public void getProductWithIdNotInExternalAPI() {

		Client client = ClientBuilder.newClient();
		WebTarget resource = client
				.target("http://localhost:8080/products/v1/");
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		Product postedProduct = getProductNotInExternalAPI();
		Gson gson = new Gson();
		String jsonPostedProduct = gson.toJson(postedProduct);
		// Post the product
		Response response = request.post(
				Entity.entity(jsonPostedProduct, MediaType.APPLICATION_JSON),
				Response.class);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		resource = client.target("http://localhost:8080/products/v1/23456532");
		request = resource.request();
		// Get the product
		response = request.get();
		assertThat(response.getStatus()).isEqualTo(
				HttpStatus.BAD_REQUEST.value());

		resource = client.target("http://localhost:8080/products/v1/23456532");
		request = resource.request();
		// Delete the product
		response = request.delete();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	}

	@Test
	public void updateProduct() {

		Client client = ClientBuilder.newClient();
		WebTarget resource = client
				.target("http://localhost:8080/products/v1/");
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		Product postedProduct = getBasicProduct();
		Gson gson = new Gson();
		String jsonPostedProduct = gson.toJson(postedProduct);
		// Post the product
		Response response = request.post(
				Entity.entity(jsonPostedProduct, MediaType.APPLICATION_JSON),
				Response.class);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

		resource = client.target("http://localhost:8080/products/v1/13860428");
		request = resource.request();
		Product toUpdateProduct = getProductNotInExternalAPI();
		String jsonToUpdateProduct = gson.toJson(toUpdateProduct);
		// Update the product
		response = request.put(
				Entity.entity(jsonToUpdateProduct, MediaType.APPLICATION_JSON),
				Response.class);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		String updatedJsonProduct = response.readEntity(String.class);
		Product updatedProduct = gson.fromJson(updatedJsonProduct,
				Product.class);
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(updatedProduct.getCurrent_price()).isEqualTo(
				toUpdateProduct.getCurrent_price());

		resource = client.target("http://localhost:8080/products/v1/13860428");
		request = resource.request();
		// Delete the product
		response = request.delete();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	}

	@Test
	public void updateProductNotFound() {

		Client client = ClientBuilder.newClient();

		WebTarget resource = client
				.target("http://localhost:8080/products/v1/13860428");
		Builder request = resource.request();
		Product toUpdateProduct = getProductNotInExternalAPI();
		Gson gson = new Gson();
		String jsonToUpdateProduct = gson.toJson(toUpdateProduct);
		// Update the product
		Response response = request.put(
				Entity.entity(jsonToUpdateProduct, MediaType.APPLICATION_JSON),
				Response.class);
		assertThat(response.getStatus())
				.isEqualTo(HttpStatus.NOT_FOUND.value());

	}

}
