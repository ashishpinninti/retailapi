package com.ashishpinninti.myretail.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ashishpinninti.myretail.MyretailConstants;
import com.ashishpinninti.myretail.entity.Product;
import com.ashishpinninti.myretail.service.ProductService;

/**
 * This class is the single end point for performing CRUD operations on product
 * details
 * 
 * @author apinninti
 */
@RestController
@RequestMapping(value = MyretailConstants.PRODUCT_CONTROLLER_PATH)
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * Fetches Product for a given id.
	 * 
	 * @param id
	 * @return Product from DB
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public Product getProduct(@PathVariable("id") String id) {
		return productService.getProduct(id);
	}

	/**
	 * Fetches all the products when page and size are not provided. Fetches
	 * products in paginated manner when both page and size are provided.
	 * 
	 * @param page
	 * @param size
	 * @return All the Products from DB
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public List<Product> getAllProducts(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		return productService.getAllProducts(page, size);
	}

	/**
	 * Saves the given product to DB
	 * 
	 * @param product
	 *            to be saved to DB
	 * @return product saved in the DB
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public Product addProduct(@RequestBody Product product) {
		return productService.addProduct(product);
	}

	/**
	 * Takes the given product details and updates it to DB.
	 * 
	 * @param id
	 * @param Product
	 *            details that are to be updated.
	 * @return Updated product
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public Product updateProduct(@PathVariable("id") String id,
			@RequestBody Product productUpdate) {
		return productService.updateProduct(id, productUpdate);
	}

	/**
	 * Deletes the given product from DB.
	 * 
	 * @param id
	 * @return delete confirmation message
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteProduct(@PathVariable("id") String id) {
		return productService.deleteProduct(id);
	}

}
