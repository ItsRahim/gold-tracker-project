package com.rahim.common.service.hazelcast;

import com.hazelcast.collection.ISet;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.rahim.common.model.HzPersistenceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Rahim Ahmed
 * @created 06/05/2024
 */
@Service
public class HazelcastFailover {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastFailover.class);
    private final HzPersistenceService setResilienceService;
    private final HzPersistenceService mapResilienceService;

    private static final AtomicReference<HazelcastInstance> hazelcastInstance = new AtomicReference<>();
    private static final String CLUSTER_NAME = "fallback-cluster";

    @Autowired
    public HazelcastFailover(@Qualifier("hzSetResilienceService") HzPersistenceService setResilienceService,
                             @Qualifier("hzMapResilienceService") HzPersistenceService mapResilienceService) {
        this.setResilienceService = setResilienceService;
        this.mapResilienceService = mapResilienceService;
    }

    private static HazelcastInstance hazelcastInstance() {
        if (hazelcastInstance.get() == null) {
            synchronized (HazelcastFailover.class) {
                if (hazelcastInstance.get() == null) {
                    Config localConfig = new Config();
                    localConfig.setClusterName(CLUSTER_NAME);
                    hazelcastInstance.set(Hazelcast.newHazelcastInstance(localConfig));
                }
            }
        }
        return hazelcastInstance.get();
    }

    public void shutdownInstance() {
        HazelcastInstance instance = hazelcastInstance.get();
        if (instance != null) {
            instance.shutdown();
            hazelcastInstance.set(null);
        }
    }

    public <T> ISet<T> getSet(String setName) {
        return hazelcastInstance().getSet(setName);
    }

    public void addToSet(String setName, Object value) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createSetPersistenceModel(setName, value, HzPersistenceModel.ObjectOperation.CREATE);
        setResilienceService.persistToDB(persistenceModel);
        LOG.debug("Adding {} to {} HazelcastConstant set...", value, setName);
        ISet<Object> set = getSet(setName);
        set.add(value);
    }

    public void removeFromSet(String setName, Object value) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createSetPersistenceModel(setName, value, HzPersistenceModel.ObjectOperation.DELETE);
        setResilienceService.removeFromDB(persistenceModel);
        LOG.debug("Removing {} from {} HazelcastConstant set...", value, setName);
        ISet<Object> set = getSet(setName);
        set.remove(value);
    }

    public void clearSet(String setName) {
        LOG.debug("Clearing {} HazelcastConstant set...", setName);
        ISet<Object> set = getSet(setName);
        set.clear();
    }

    public <K, V> IMap<K, V> getMap(String mapName) {
        return hazelcastInstance().getMap(mapName);
    }

    public void addToMap(String mapName, String key, Object value) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createMapPersistenceModel(mapName, key, value, HzPersistenceModel.ObjectOperation.CREATE);
        mapResilienceService.persistToDB(persistenceModel);
        IMap<Object, Object> map = getMap(mapName);
        map.put(key, value);
        LOG.debug("Added key: {} with value: {} to map: {}", key, value, mapName);
    }
    
    public void removeFromMap(String mapName, String key) {
        IMap<Object, Object> map = getMap(mapName);
        map.remove(key);
        LOG.debug("Removed key: {} from map: {}", key, mapName);
    }

    public void clearMap(String mapName) {
        IMap<Object, Object> map = getMap(mapName);
        map.clear();
        LOG.debug("Cleared map: {}", mapName);
    }
}
