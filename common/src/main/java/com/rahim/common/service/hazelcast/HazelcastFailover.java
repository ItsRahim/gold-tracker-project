package com.rahim.common.service.hazelcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Rahim Ahmed
 * @created 06/05/2024
 */
@Service
public class HazelcastFailover {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastFailover.class);

    private final Map<String, Set<Object>> setStorage = new HashMap<>();
    private final Map<String, Map<String, Object>> mapStorage = new HashMap<>();

    public <T> Set<T> getSet(String setName) {
        LOG.info("RUNNING FALLBACK: Retrieving set '{}' from storage", setName);
        return (Set<T>) setStorage.getOrDefault(setName, new HashSet<>());
    }

    public void addToSet(String setName, Object value) {
        LOG.info("RUNNING FALLBACK: Adding value '{}' to set '{}'", value, setName);
        setStorage.computeIfAbsent(setName, k -> new HashSet<>()).add(value);
    }

    public void removeFromSet(String setName, Object value) {
        LOG.info("RUNNING FALLBACK: Removing value '{}' from set '{}'", value, setName);
        Set<Object> set = setStorage.get(setName);
        if (set != null) {
            set.remove(value);
        }
    }

    public void clearSet(String setName) {
        LOG.info("RUNNING FALLBACK: Clearing set '{}'", setName);
        setStorage.remove(setName);
    }

    public <K, V> Map<K, V> getMap(String mapName) {
        LOG.info("RUNNING FALLBACK: Retrieving map '{}' from storage", mapName);
        return (Map<K, V>) mapStorage.getOrDefault(mapName, new HashMap<>());
    }

    public void addToMap(String mapName, String key, Object value) {
        LOG.info("RUNNING FALLBACK: Adding value '{}' with key '{}' to map '{}'", value, key, mapName);
        mapStorage.computeIfAbsent(mapName, k -> new HashMap<>()).put(key, value);
    }

    public void removeFromMap(String mapName, String key) {
        LOG.info("RUNNING FALLBACK: Removing value with key '{}' from map '{}'", key, mapName);
        Map<String, Object> map = mapStorage.get(mapName);
        if (map != null) {
            map.remove(key);
        }
    }

    public void clearMap(String mapName) {
        LOG.info("RUNNING FALLBACK: Clearing map '{}'", mapName);
        mapStorage.remove(mapName);
    }
}
