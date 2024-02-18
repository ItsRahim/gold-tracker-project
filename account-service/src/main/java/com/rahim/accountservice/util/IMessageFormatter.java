package com.rahim.accountservice.util;

import java.util.Map;

public interface IMessageFormatter {

    /**
     * Updates a key in the given map. If the old key is present, it removes the old key-value pair and puts the new key-value pair into the map.
     *
     * @param map The map where the key needs to be updated.
     * @param oldKey The old key that needs to be replaced.
     * @param newKey The new key that will replace the old key.
     */
    void updateMapKey(Map<String, Object> map, String oldKey, String newKey);

    /**
     * Formats the value of a given key in the map if it is an instance of Instant or Date. The formatted value is then put back into the map.
     *
     * @param map The map containing the key-value pair to be formatted.
     * @param key The key of the value to be formatted.
     */
    void formatInstant(Map<String, Object> map, String key);
}
