package com.rahim.emailservice.exception;

/**
 * @author Rahim Ahmed
 * @created 19/02/2024
 */
public class EmailTemplateException extends RuntimeException{
    public EmailTemplateException(String message) {
        super(message);
    }

    public EmailTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
