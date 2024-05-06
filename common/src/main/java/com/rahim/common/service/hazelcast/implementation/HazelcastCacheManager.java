package com.rahim.common.service.hazelcast.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.rahim.common.model.HzPersistenceModel;
import com.rahim.common.service.hazelcast.ICacheManager;
import com.rahim.common.service.hazelcast.HzPersistenceService;
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
public class HazelcastCacheManager implements ICacheManager {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastCacheManager.class);
    private final HazelcastInstance hazelcastInstance;
    private final HzPersistenceService setResilienceService;
    private final HzPersistenceService mapResilienceService;

    private boolean healthy = true;

    @Autowired
    public HazelcastCacheManager(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance,
                                 @Qualifier("hzSetResilienceService") HzPersistenceService setResilienceService,
                                 @Qualifier("hzMapResilienceService") HzPersistenceService mapResilienceService) {
        this.hazelcastInstance = hazelcastInstance;
        this.setResilienceService = setResilienceService;
        this.mapResilienceService = mapResilienceService;
    }

    @Override
    public HazelcastInstance getInstance() {
        return hazelcastInstance;
    }

    @Override
    public <T> ISet<T> getSet(String setName) {
        return hazelcastInstance.getSet(setName);
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

    @Override
    public <K, V> IMap<K, V> getMap(String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    @Override
    public void addToMap(String mapName, String key, Object value) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createMapPersistenceModel(mapName, key, value, HzPersistenceModel.ObjectOperation.CREATE);
        mapResilienceService.persistToDB(persistenceModel);
        IMap<Object, Object> map = getMap(mapName);
        map.put(key, value);
        LOG.debug("Added key: {} with value: {} to map: {}", key, value, mapName);
    }

    @Override
    public void removeFromMap(String mapName, String key) {
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

    @Override
    public boolean isHealthy() {
        return healthy;
    }

    @Override
    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

}
