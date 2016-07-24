package com.ashishpinninti.myretail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Product already exist")
// constants
public class ProductAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = -3404984652070629894L;

	public ProductAlreadyExistException(String message) {
		super(message);
	}

	public ProductAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
