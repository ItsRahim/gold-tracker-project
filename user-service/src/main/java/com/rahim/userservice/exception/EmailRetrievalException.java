package com.rahim.userservice.exception;

public class EmailRetrievalException extends RuntimeException {
    public EmailRetrievalException(String message) {
        super(message);
    }

    public EmailRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
