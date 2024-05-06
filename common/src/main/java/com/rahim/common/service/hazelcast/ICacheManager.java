package com.rahim.common.service.hazelcast;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
public interface ICacheManager {
    HazelcastInstance getInstance();

    <T> ISet<T> getSet(String setName);
    void addToSet(String setName, Object value);
    void removeFromSet(String setName, Object value);
    void clearSet(String setName);

    <K, V> IMap<K, V> getMap(String mapName);
    void addToMap(String mapName, String key, Object value);
    void removeFromMap(String mapName, String key);
    void clearMap(String mapName);

    boolean isHealthy();
    void setHealthy(boolean healthy);
}
