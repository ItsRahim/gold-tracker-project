package com.rahim.common.util;

import java.util.UUID;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
public class KafkaKeyUtil {

    public static String generateKeyWithUUID(String message) {
        return message + "_" + UUID.randomUUID();
    }

    public static String extractDataFromKey(String key) {
        String[] parts = key.split("_");
        return parts[0];
    }
}
