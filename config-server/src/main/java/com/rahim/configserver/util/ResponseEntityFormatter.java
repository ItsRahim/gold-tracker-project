package com.rahim.configserver.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Rahim Ahmed
 * @created 22/04/2024
 */
public class ResponseEntityFormatter {

    private ResponseEntityFormatter() {}

    public static ResponseEntity<Object> jsonFormatter(HttpStatus status, String message) {
        return ResponseEntity.status(status).body("{\"Response\": \"" + message + "\"}");
    }
}
