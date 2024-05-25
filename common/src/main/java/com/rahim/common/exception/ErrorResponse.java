package com.rahim.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Rahim Ahmed
 * @created 24/05/2024
 */
@Getter
@Setter
@Builder
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    @Builder.Default
    private long timestamp = System.currentTimeMillis();

}
