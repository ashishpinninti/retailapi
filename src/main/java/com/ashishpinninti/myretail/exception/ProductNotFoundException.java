package com.ashishpinninti.myretail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Product doesn't exist")
public class ProductNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -4439627416851031768L;

	public ProductNotFoundException(String message) {
		super(message);
	}

	public ProductNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
