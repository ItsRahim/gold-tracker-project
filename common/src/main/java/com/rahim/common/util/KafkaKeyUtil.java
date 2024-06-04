package com.rahim.common.util;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
public class KafkaKeyUtil {

    private KafkaKeyUtil() {}

    public static String extractDataFromKey(String message) {
        String[] parts = message.split(",", 2);

        if (parts.length < 2 || parts[1].isEmpty()) {
            return message;
        } else {
            return parts[1];
        }
    }
}
