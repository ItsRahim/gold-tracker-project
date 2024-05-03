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
    void addToSet(Object value, String setName);
    void removeFromSet(Object value, String setName);
    void clearSet(String setName);

    <K, V> IMap<K, V> getMap(String mapName);
    void addToMap(String mapName, Object key, Object value);
    void removeFromMap(Object key, String mapName);
    void clearMap(String mapName);
}
