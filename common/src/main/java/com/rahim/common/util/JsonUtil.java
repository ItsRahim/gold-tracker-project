package com.rahim.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahim.common.exception.JsonServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahim Ahmed
 * @created 27/05/2024
 */
public class JsonUtil {

    private JsonUtil() {}

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOG.error("An error occurred serialising: {}", object);
            throw new JsonServiceException("Error occurred serialising data");
        }
    }

    public static <T> T convertJsonToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            LOG.error("An error occurred de-serialising {} to {}", json, clazz);
            throw new JsonServiceException("Error occurred de-serialising data");
        }
    }
}
