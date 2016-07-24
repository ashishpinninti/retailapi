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

import com.ashishpinninti.myretail.entity.Product;
import com.ashishpinninti.myretail.service.UserService;

@RestController
@RequestMapping(value = "/products/v1")
// v1 version // constants
public class ProductController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public Product getProduct(@PathVariable("id") String id) {
		return userService.getProduct(id);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public List<Product> getAllProducts(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		return userService.getAllProducts(page, size);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
	public Product addProduct(@RequestBody Product product) {
		return userService.addProduct(product);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public Product updateProduct(@PathVariable("id") String id,
			@RequestBody Product productUpdate) {
		return userService.updateProduct(id, productUpdate);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteProduct(@PathVariable("id") String id) {
		return userService.deleteProduct(id);
	}

}
