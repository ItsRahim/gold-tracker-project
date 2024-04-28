package com.rahim.accountservice.util;

import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
public interface IMessageFormatter {

    /**
     * Formats the value of a given key in the map if it is an instance of Instant or Date. The formatted value is then put back into the map.
     *
     * @param map The map containing the key-value pair to be formatted.
     * @param key The key of the value to be formatted.
     */
    void formatInstant(Map<String, Object> map, String key);
}
