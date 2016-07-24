package com.ashishpinninti.myretail.service;

import java.util.List;

import com.ashishpinninti.myretail.entity.Product;

public interface UserService {

	Product updateProduct(String id, Product productUpdate);

	Product addProduct(Product product);

	Product getProduct(String id);

	List<Product> getAllProducts(Integer page, Integer size);

	String deleteProduct(String id);

}
