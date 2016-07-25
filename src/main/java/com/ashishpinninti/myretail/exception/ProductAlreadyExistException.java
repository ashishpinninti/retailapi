package com.ashishpinninti.myretail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ashishpinninti.myretail.MyretailConstants;

/**
 * Exception raised when Product already exists.
 * 
 * @author apinninti
 *
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = MyretailConstants.EX_REASON_PRODUCT_ALREADY_EXIST)
public class ProductAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = -3404984652070629894L;

	public ProductAlreadyExistException(String message) {
		super(message);
	}

	public ProductAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

}
