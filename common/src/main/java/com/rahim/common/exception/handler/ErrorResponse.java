package com.rahim.common.exception.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Rahim Ahmed
 * @created 24/05/2024
 */
@Getter
@Setter
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private long timestamp;

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }
}
