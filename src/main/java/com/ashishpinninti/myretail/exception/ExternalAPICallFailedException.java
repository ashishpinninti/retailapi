package com.ashishpinninti.myretail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ashishpinninti.myretail.MyretailConstants;

/**
 * Exception class representing External API call fail.
 * 
 * @author apinninti
 *
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = MyretailConstants.EX_REASON_CALL_TO_EXTERNAL_API_FAILED)
public class ExternalAPICallFailedException extends RuntimeException {

	private static final long serialVersionUID = 3462816139998010587L;

	public ExternalAPICallFailedException(String message) {
		super(message);
	}

	public ExternalAPICallFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}
