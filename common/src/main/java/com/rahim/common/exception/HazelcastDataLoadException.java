package com.rahim.common.exception;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class HazelcastDataLoadException extends RuntimeException {
    public HazelcastDataLoadException(String message) {
        super(message);
    }

    public HazelcastDataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}

