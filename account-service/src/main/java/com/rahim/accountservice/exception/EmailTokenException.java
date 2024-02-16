package com.rahim.accountservice.exception;

public class EmailTokenException extends RuntimeException {
    public EmailTokenException(String message) {
        super(message);
    }

    public EmailTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
