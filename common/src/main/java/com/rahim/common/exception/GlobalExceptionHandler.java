package com.rahim.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Rahim Ahmed
 * @created 24/05/2024
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(httpStatus)
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(httpStatus)
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
