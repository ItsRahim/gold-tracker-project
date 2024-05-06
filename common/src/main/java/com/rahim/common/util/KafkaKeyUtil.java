package com.rahim.common.util;

import java.util.UUID;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
public class KafkaKeyUtil {

    private KafkaKeyUtil() {}

    public static String generateKeyWithUUID(String message) {
        return message + "_" + UUID.randomUUID();
    }

    public static String extractDataFromKey(String message) {
        int lastIndex = message.lastIndexOf("_");
        if (lastIndex != -1) {
            return message.substring(0, lastIndex);
        } else {
            return message;
        }
    }
}
