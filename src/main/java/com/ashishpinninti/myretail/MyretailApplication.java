package com.ashishpinninti.myretail;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ashishpinninti.myretail.entity.CurrentPrice;
import com.ashishpinninti.myretail.entity.Product;
import com.ashishpinninti.myretail.repository.ProductRepository;

@SpringBootApplication
public class MyretailApplication implements CommandLineRunner {

	@Autowired
	private ProductRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(MyretailApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		// initialize seed in another method in another class
		
		repository.deleteAll();

		CurrentPrice c1 = new CurrentPrice("1", "USD");
		CurrentPrice c2 = new CurrentPrice("2", "INR");
		CurrentPrice c3 = new CurrentPrice("3", "USD");
		CurrentPrice c4 = new CurrentPrice("4", "INR");
		CurrentPrice c5 = new CurrentPrice("5", "USD");
		CurrentPrice c6 = new CurrentPrice("6", "INR");
		
		repository.save(new Product(new BigInteger("13860428"), c1));
		repository.save(new Product(new BigInteger("15117729"), c2));
		repository.save(new Product(new BigInteger("16483589"), c3));
		repository.save(new Product(new BigInteger("16696652"), c4));
		repository.save(new Product(new BigInteger("16752456"), c5));
		repository.save(new Product(new BigInteger("15643793"), c6));

		System.out.println("Products found with findAll():");
		System.out.println("-------------------------------");
		for (Product product : repository.findAll()) {
			System.out.println(product);
		}
		System.out.println();
	}
}
