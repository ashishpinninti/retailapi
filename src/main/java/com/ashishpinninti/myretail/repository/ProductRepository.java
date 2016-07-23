package com.ashishpinninti.myretail.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ashishpinninti.myretail.entity.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
    
}
