package com.rahim.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class DuplicateEntityException extends BaseException {

    public DuplicateEntityException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
