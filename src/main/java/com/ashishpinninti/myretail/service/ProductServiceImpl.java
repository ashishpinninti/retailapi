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

/**
 * Product service call for performing major business operations and error
 * handling.
 * 
 * @author apinninti
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	/**
	 * Fetches Product for a given id. Performs external Target API call for
	 * fetching product name.
	 * 
	 * @param id
	 * @return Product from DB
	 * @throws ProductNotFoundException
	 *             and ExternalAPICallFailedException
	 */
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

	/**
	 * Fetches all the products when page and size are not provided. Fetches
	 * products in paginated manner when both page and size are provided.
	 * 
	 * @param page
	 * @param size
	 * @return All the Products from DB
	 */
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

	/**
	 * Saves the given product to DB.
	 * 
	 * @param product
	 *            to be saved to DB
	 * @return product saved in the DB
	 * @throws ProductAlreadyExistException
	 */
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

	/**
	 * Takes the given product details and updates it to DB.
	 * 
	 * @param id
	 * @param Product
	 *            details that are to be updated.
	 * @return Updated product
	 * @throws ProductNotFoundException
	 */
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

	/**
	 * Deletes the given product from DB.
	 * 
	 * @param id
	 * @return delete confirmation message
	 * @throws ProductNotFoundException
	 */
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

	/**
	 * Performs external call to Target API and fetches product name for a given
	 * ID.
	 * 
	 * @param productId
	 * @return productName
	 * @throws ExternalAPICallFailedException
	 */
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