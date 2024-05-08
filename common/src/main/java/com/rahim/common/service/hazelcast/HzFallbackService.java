package com.rahim.common.service.hazelcast;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;

/**
 * @author Rahim Ahmed
 * @created 06/05/2024
 */
public interface HzFallbackService {
    <T> ISet<T> fallbackGetSet(String setName);
    void fallbackAddToSet(String setName, Object value);
    void fallbackRemoveFromSet(String setName, Object value);
    void fallbackClearSet(String setName);

    <K, V> IMap<K, V> fallbackGetMap(String mapName);
    void fallbackAddToMap(String mapName, String key, Object value);
    void fallbackRemoveFromMap(String mapName, String key);
    void fallbackClearMap(String mapName);
}
