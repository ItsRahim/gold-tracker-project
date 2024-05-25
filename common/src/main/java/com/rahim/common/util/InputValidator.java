package com.rahim.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class InputValidator {

    private InputValidator() {}

    private static final Logger LOG = LoggerFactory.getLogger(InputValidator.class);

    public static boolean validateFields(Object object, String... requiredFields) {
        for (String fieldName : requiredFields) {
            try {
                Field field = object.getClass().getDeclaredField(fieldName);
                Object value = field.get(object);

                if (value == null || (value instanceof String string && StringUtils.isBlank(string))) {
                    LOG.warn("Null or blank value found for required field: {}", fieldName);
                    return true;
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOG.error("Error accessing field: {}", fieldName, e);
                return true;
            }
        }
        return false;
    }
}
