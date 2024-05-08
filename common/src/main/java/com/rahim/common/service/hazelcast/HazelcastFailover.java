package com.rahim.common.service.hazelcast;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 06/05/2024
 */
@Service
public class HazelcastFailover {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastFailover.class);

    public <T> ISet<T> getSet(String setName) {
        LOG.info("RUNNING FALLBACK");
        return null;
    }

    public void addToSet(String setName, Object value) {
        LOG.info("RUNNING FALLBACK");

    }

    public void removeFromSet(String setName, Object value) {
        LOG.info("RUNNING FALLBACK");
    }

    public void clearSet(String setName) {
        LOG.info("RUNNING FALLBACK");
    }

    public <K, V> IMap<K, V> getMap(String mapName) {
        LOG.info("RUNNING FALLBACK");
        return null;
    }

    public void addToMap(String mapName, String key, Object value) {
        LOG.info("RUNNING FALLBACK");
    }

    public void removeFromMap(String mapName, String key) {
        LOG.info("RUNNING FALLBACK");
    }

    public void clearMap(String mapName) {
        LOG.info("RUNNING FALLBACK");
    }
}
