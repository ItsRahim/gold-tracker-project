package com.rahim.common.exception;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class JsonServiceException extends BaseServiceException {

    public JsonServiceException(String message) {
        super(message);
    }

    public JsonServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
