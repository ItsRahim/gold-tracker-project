package com.rahim.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Rahim Ahmed
 * @created 24/05/2024
 */
public class ValidationException extends BaseException {

    protected ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
