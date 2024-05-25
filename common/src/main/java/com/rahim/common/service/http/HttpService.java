package com.rahim.common.service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahim.common.exception.HttpServiceException;
import com.rahim.common.exception.JsonServiceException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Rahim Ahmed
 * @created 22/05/2024
 */
@Service
@RequiredArgsConstructor
public class HttpService {

    private static final Logger LOG = LoggerFactory.getLogger(HttpService.class);
    private final ObjectMapper objectMapper;

    public <T> T fetchValueFromService(Supplier<ResponseEntity<String>> serviceCall, Function<JsonNode, T> valueExtractor) {
        try {
            ResponseEntity<String> response = serviceCall.get();
            HttpStatusCode httpStatusCode = response.getStatusCode();

            if (httpStatusCode.is2xxSuccessful()) {
                LOG.error("Error retrieving data, status code: {}", response.getStatusCode());
                throw new HttpServiceException("Error retrieving data, status code: " + httpStatusCode);
            }

            String jsonResponse = response.getBody();
            LOG.debug("Received response: {}", jsonResponse);
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            return valueExtractor.apply(jsonNode);
        } catch (JsonProcessingException e) {
            LOG.error("Error processing JSON response: {}", e.getMessage(), e);
            throw new JsonServiceException("Error processing JSON response");
        } catch (Exception e) {
            LOG.error("Error retrieving data: {}", e.getMessage(), e);
            throw new HttpServiceException("Unexpected error retrieving data");
        }
    }
}
