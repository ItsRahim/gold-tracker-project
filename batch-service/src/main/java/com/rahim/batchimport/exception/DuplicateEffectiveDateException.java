package com.rahim.batchimport.exception;

public class DuplicateEffectiveDateException extends RuntimeException {

    public DuplicateEffectiveDateException(String message) {
        super(message);
    }

    public DuplicateEffectiveDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
