package com.rahim.common.service.hazelcast.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.rahim.common.service.hazelcast.HzFallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 06/05/2024
 */
@Service
public class HzFallbackServiceImpl implements HzFallbackService {

    private static final Logger LOG = LoggerFactory.getLogger(HzFallbackServiceImpl.class);

    @Override
    public <T> ISet<T> fallbackGetSet(String setName) {
        LOG.info("RUNNING FALLBACK");
        return null;
    }

    @Override
    public void fallbackAddToSet(String setName, Object value) {
        LOG.info("RUNNING FALLBACK");

    }

    @Override
    public void fallbackRemoveFromSet(String setName, Object value) {
        LOG.info("RUNNING FALLBACK");
    }

    @Override
    public void fallbackClearSet(String setName) {
        LOG.info("RUNNING FALLBACK");
    }

    @Override
    public <K, V> IMap<K, V> fallbackGetMap(String mapName) {
        LOG.info("RUNNING FALLBACK");
        return null;
    }

    @Override
    public void fallbackAddToMap(String mapName, String key, Object value) {
        LOG.info("RUNNING FALLBACK");
    }

    @Override
    public void fallbackRemoveFromMap(String mapName, String key) {
        LOG.info("RUNNING FALLBACK");
    }

    @Override
    public void fallbackClearMap(String mapName) {
        LOG.info("RUNNING FALLBACK");
    }
}
