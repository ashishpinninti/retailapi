package com.ashishpinninti.myretail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Call to external API failed!")
// constants
public class ExternalAPICallFailedException extends RuntimeException {

	private static final long serialVersionUID = 3462816139998010587L;

	public ExternalAPICallFailedException(String message) {
		super(message);
	}

	public ExternalAPICallFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}
