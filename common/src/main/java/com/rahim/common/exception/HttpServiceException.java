package com.rahim.common.exception;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class HttpServiceException extends BaseServiceException{

    public HttpServiceException(String message) {
        super(message);
    }

    public HttpServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
