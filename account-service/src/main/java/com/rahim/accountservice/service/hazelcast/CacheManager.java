package com.rahim.accountservice.service.hazelcast;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
public interface CacheManager {

    HazelcastInstance getInstance();

    <T> ISet<T> getSet(String setName);
    void addToSet(String setName, Object value);
    void removeFromSet(String setName, Object value);
    void clearSet(String setName);

    <K, V> IMap<K, V> getMap(String mapName);
    void addToMap(String mapName, Object key, Object value);
    void removeFromMap(String mapName, Object key);
    void clearMap(String mapName);
}
