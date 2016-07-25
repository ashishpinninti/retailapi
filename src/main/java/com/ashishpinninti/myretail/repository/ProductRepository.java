package com.ashishpinninti.myretail.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ashishpinninti.myretail.entity.Product;

/**
 * Product repository for performing basic DB operations.
 * 
 * @author apinninti
 *
 */
public interface ProductRepository extends MongoRepository<Product, String> {

}
