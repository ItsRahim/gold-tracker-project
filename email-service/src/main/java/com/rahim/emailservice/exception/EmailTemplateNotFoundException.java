package com.rahim.emailservice.exception;

/**
 * @author Rahim Ahmed
 * @created 19/02/2024
 */
public class EmailTemplateNotFoundException extends RuntimeException {

    public EmailTemplateNotFoundException(String message) {
        super(message);
    }

    public EmailTemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
