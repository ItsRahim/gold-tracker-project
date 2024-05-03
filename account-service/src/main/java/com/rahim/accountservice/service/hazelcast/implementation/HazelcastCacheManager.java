package com.rahim.accountservice.service.hazelcast.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.rahim.accountservice.service.hazelcast.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
@Service
public class HazelcastCacheManager implements CacheManager {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastCacheManager.class);
    private final HazelcastInstance hazelcastInstance;

    @Autowired
    public HazelcastCacheManager(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public HazelcastInstance getInstance() {
        return hazelcastInstance;
    }

    @Override
    public <T> ISet<T> getSet(String setName) {
        return hazelcastInstance.getSet(setName);
    }

    public void addToSet(Object value, String setName) {
        LOG.debug("Adding {} to {} Hazelcast set...", value, setName);
        ISet<Object> set = getSet(setName);
        set.add(value);
    }

    public void removeFromSet(Object value, String setName) {
        LOG.debug("Removing {} from {} Hazelcast set...", value, setName);
        ISet<Object> set = getSet(setName);
        set.remove(value);
    }

    public void clearSet(String setName) {
        LOG.debug("Clearing {} Hazelcast set...", setName);
        ISet<Object> set = getSet(setName);
        set.clear();
    }

    @Override
    public <K, V> IMap<K, V> getMap(String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    @Override
    public void addToMap(String mapName, Object key, Object value) {
        IMap<Object, Object> map = getMap(mapName);
        map.put(key, value);
        LOG.debug("Added key: {} with value: {} to map: {}", key, value, mapName);
    }

    @Override
    public void removeFromMap(Object key, String mapName) {
        IMap<Object, Object> map = getMap(mapName);
        map.remove(key);
        LOG.debug("Removed key: {} from map: {}", key, mapName);
    }

    @Override
    public void clearMap(String mapName) {
        IMap<Object, Object> map = getMap(mapName);
        map.clear();
        LOG.debug("Cleared map: {}", mapName);
    }

}
