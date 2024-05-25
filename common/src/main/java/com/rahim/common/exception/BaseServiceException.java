package com.rahim.common.exception;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class BaseServiceException extends RuntimeException {

    public BaseServiceException(String message) {
        super(message);
    }

    public BaseServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
