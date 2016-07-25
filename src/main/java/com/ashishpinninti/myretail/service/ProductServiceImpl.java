package com.ashishpinninti.myretail.service;

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
import org.springframework.stereotype.Service;

import com.ashishpinninti.myretail.entity.Product;
import com.ashishpinninti.myretail.exception.ExternalAPICallFailedException;
import com.ashishpinninti.myretail.exception.ProductAlreadyExistException;
import com.ashishpinninti.myretail.exception.ProductNotFoundException;
import com.ashishpinninti.myretail.repository.ProductRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product getProduct(String id) {
		Product product = productRepository.findOne(id);
		if (product == null) {
			throw new ProductNotFoundException("Product with id=" + id
					+ " not found!");
		}
		BigInteger productId = product.getId();
		String productNameFromTargetAPI = getProductNameFromTargetAPI(productId);
		product.setName(productNameFromTargetAPI);
		productRepository.save(product);
		return product;
	}

	@Override
	public List<Product> getAllProducts(Integer page, Integer size) {
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

	@Override
	public Product addProduct(Product product) {
		BigInteger id = product.getId();
		if (id != null) {
			Product dbProduct = productRepository.findOne(id.toString());
			if (dbProduct != null) {
				throw new ProductAlreadyExistException("Product with id=" + id
						+ " already exists!");
			}
			// String productNameFromTargetAPI =
			// getProductNameFromTargetAPI(id);
			// product.setName(productNameFromTargetAPI);
		}
		Product insert = productRepository.insert(product);
		return insert;
	}

	@Override
	public Product updateProduct(String id, Product productUpdate) {
		Product product = productRepository.findOne(id);
		if (product == null) {
			throw new ProductNotFoundException("Product with id=" + id
					+ " not found!");
		}
		product.setCurrent_price(productUpdate.getCurrent_price());
		productRepository.save(product);
		return product;
	}

	@Override
	public String deleteProduct(String id) {
		Product product = productRepository.findOne(id);
		if (product == null) {
			throw new ProductNotFoundException("Product with id=" + id
					+ " not found!");
		}
		productRepository.delete(id);
		return "deleted product with id: " + id;
	}

	private String getProductNameFromTargetAPI(BigInteger productId) {
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
}