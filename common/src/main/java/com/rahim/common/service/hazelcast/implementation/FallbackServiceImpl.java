package com.rahim.common.service.hazelcast.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.rahim.common.service.hazelcast.IFallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 06/05/2024
 */
@Service
public class FallbackServiceImpl implements IFallbackService {

    private static final Logger LOG = LoggerFactory.getLogger(FallbackServiceImpl.class);

    @Override
    public <T> ISet<T> fallbackGetSet(String setName) {
        return null;
    }

    @Override
    public void fallbackAddToSet(String setName, Object value) {

    }

    @Override
    public void fallbackRemoveFromSet(String setName, Object value) {

    }

    @Override
    public void fallbackClearSet(String setName) {

    }

    @Override
    public <K, V> IMap<K, V> fallbackGetMap(String mapName) {
        return null;
    }

    @Override
    public void fallbackAddToMap(String mapName, String key, Object value) {

    }

    @Override
    public void fallbackRemoveFromMap(String mapName, String key) {

    }

    @Override
    public void fallbackClearMap(String mapName) {

    }
}
