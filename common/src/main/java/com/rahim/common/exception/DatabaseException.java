package com.rahim.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class DatabaseException extends BaseException {

    public DatabaseException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
