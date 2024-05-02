package com.rahim.accountservice.service.hazelcast.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.rahim.accountservice.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
@Service
@RequiredArgsConstructor
public class HazelcastCacheManager implements CacheManager {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastCacheManager.class);
    private final HazelcastInstance hazelcastInstance;

    public void addToSet(Object value, String setName) {
        LOG.debug("Adding {} to {} Hazelcast set...", value, setName);
        ISet<Object> set = hazelcastInstance.getSet(setName);
        set.add(value);
    }

    public void removeFromSet(Object value, String setName) {
        LOG.debug("Removing {} from {} Hazelcast set...", value, setName);
        ISet<Object> set = hazelcastInstance.getSet(setName);
        set.remove(value);
    }

    public void clearSet(String setName) {
        LOG.debug("Clearing {} Hazelcast set...", setName);
        ISet<Object> set = hazelcastInstance.getSet(setName);
        set.clear();
    }

    @Override
    public <T> ISet<T> getSet(String setName) {
        return hazelcastInstance.getSet(setName);
    }

}
