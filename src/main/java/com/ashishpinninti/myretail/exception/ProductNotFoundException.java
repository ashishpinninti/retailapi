package com.ashishpinninti.myretail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ashishpinninti.myretail.MyretailConstants;

/**
 * Exception raised when product doesn't exist.
 * 
 * @author apinninti
 *
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = MyretailConstants.EX_REASON_PRODUCT_DOESN_T_EXIST)
public class ProductNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4439627416851031768L;

	public ProductNotFoundException(String message) {
		super(message);
	}

	public ProductNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
