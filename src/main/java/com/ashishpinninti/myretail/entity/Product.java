package com.ashishpinninti.myretail.entity;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

public class Product {
	@Id
	private BigInteger id;
	// @Transient: Not sure whether to get the name from Target API or local DB
	private String name;
	private CurrentPrice current_price;

	public Product() {
	}

	public Product(BigInteger id, CurrentPrice current_price) {
		super();
		this.id = id;
		this.current_price = current_price;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CurrentPrice getCurrent_price() {
		return current_price;
	}

	public void setCurrent_price(CurrentPrice current_price) {
		this.current_price = current_price;
	}
}
