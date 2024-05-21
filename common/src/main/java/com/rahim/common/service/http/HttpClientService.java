package com.rahim.common.service.http;

import org.springframework.http.ResponseEntity;

/**
 * @author Rahim Ahmed
 * @created 21/05/2024
 */
public interface HttpClientService {

    <T> ResponseEntity<T> callService(String serviceName, Class<T> responseType, String endpoint);

    <T> ResponseEntity<T> callService(String serviceName, String endpoint, Class<T> responseType, Object... pathVariables);
}
