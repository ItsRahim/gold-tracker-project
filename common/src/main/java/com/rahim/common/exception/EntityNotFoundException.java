package com.rahim.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class EntityNotFoundException extends BaseException{

    public EntityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
