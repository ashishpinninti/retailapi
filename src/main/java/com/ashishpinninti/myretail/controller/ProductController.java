package com.ashishpinninti.myretail.controller;

import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ashishpinninti.myretail.entity.Product;
import com.ashishpinninti.myretail.repository.ProductRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public Product getProduct(@PathVariable("id") String id) {
		Product product = productRepository.findOne(id);
		BigInteger productId = product.getId();
		String productNameFromTargetAPI = getProductNameFromTargetAPI(productId);
		product.setName(productNameFromTargetAPI);
		return product;
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public List<Product> getAllProducts(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		Pageable pageableRequest = null;
		Page<Product> pages = null;
		List<Product> products = null;

		if (page != null && size != null) {
			pageableRequest = new PageRequest(page, size);
			pages = productRepository.findAll(pageableRequest);
			products = pages.getContent();
		} else {
			products = productRepository.findAll();
		}

		return products;
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
	public Product addProduct(@RequestBody Product product) {
		BigInteger productId = product.getId();
		String productNameFromTargetAPI = getProductNameFromTargetAPI(productId);
		product.setName(productNameFromTargetAPI);
		Product insert = productRepository.insert(product);
		return insert;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public Product updateProduct(@PathVariable("id") String id,
			@RequestBody Product productUpdate) {
		Product product = productRepository.findOne(id);
		product.setCurrent_price(productUpdate.getCurrent_price());
		productRepository.save(product);
		return product;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteProduct(@PathVariable("id") String id) {
		productRepository.delete(id);
		return "deleted product with id: " + id;
	}

	private String getProductNameFromTargetAPI(BigInteger productId) {
		Client client = ClientBuilder.newClient();
		WebTarget resource = client
				.target("https://api.target.com/products/v3/"
						+ productId
						+ "?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz");
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		JsonParser jsonParser = new JsonParser();
		JsonObject getResponse = jsonParser.parse(request.get(String.class))
				.getAsJsonObject();
		JsonObject productCompositeResponse = getResponse
				.getAsJsonObject("product_composite_response");
		JsonArray itemsArray = productCompositeResponse.getAsJsonArray("items");
		String productName = itemsArray.get(0).getAsJsonObject()
				.getAsJsonObject("online_description").get("value")
				.getAsString();
		return productName;
	}

}
