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

import java.util.concurrent.atomic.AtomicBoolean;
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
    private static final AtomicBoolean isInitialised = new AtomicBoolean(false);
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

        if (isInitialised.compareAndSet(false, true)) {
            loadFromDb();
        }

        return hazelcastInstance.get();
    }

    private static void loadFromDb() {
        //TODO
    }

    public void shutdownInstance() {
        HazelcastInstance instance = hazelcastInstance.get();
        if (instance != null) {
            instance.shutdown();
            hazelcastInstance.set(null);
        }
        LOG.info("Shutdown failover cluster...");
    }

    public <T> ISet<T> getSet(String setName) {
        LOG.debug("Running failover: Retrieving set '{}' from storage...", setName);
        return hazelcastInstance().getSet(setName);
    }

    public void addToSet(String setName, Object value) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createSetPersistenceModel(setName, value, HzPersistenceModel.ObjectOperation.CREATE);
        setResilienceService.persistToDB(persistenceModel);
        LOG.debug("Running failover: Adding '{}' to '{}' Hazelcast set...", value, setName);
        ISet<Object> set = getSet(setName);
        set.add(value);
    }

    public void removeFromSet(String setName, Object value) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createSetPersistenceModel(setName, value, HzPersistenceModel.ObjectOperation.DELETE);
        setResilienceService.removeFromDB(persistenceModel);
        LOG.debug("Running failover: Removing '{}' from '{}' Hazelcast set...", value, setName);
        ISet<Object> set = getSet(setName);
        set.remove(value);
    }

    public void clearSet(String setName) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createSetPersistenceModel(setName, null, HzPersistenceModel.ObjectOperation.DELETE);
        setResilienceService.clearFromDB(persistenceModel);
        LOG.debug("Running failover: Clearing '{}' Hazelcast set...", setName);
        ISet<Object> set = getSet(setName);
        set.clear();
    }

    public <K, V> IMap<K, V> getMap(String mapName) {
        LOG.debug("Running failover: Retrieving map '{}' from storage...", mapName);
        return hazelcastInstance().getMap(mapName);
    }

    public void addToMap(String mapName, String key, Object value) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createMapPersistenceModel(mapName, key, value, HzPersistenceModel.ObjectOperation.CREATE);
        mapResilienceService.persistToDB(persistenceModel);
        LOG.debug("Running failover: Added key: {} with value: {} to map: {}", key, value, mapName);
        IMap<Object, Object> map = getMap(mapName);
        map.put(key, value);
    }

    public void removeFromMap(String mapName, String key) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createMapPersistenceModel(mapName, key, null, HzPersistenceModel.ObjectOperation.DELETE);
        mapResilienceService.removeFromDB(persistenceModel);
        LOG.debug("Running failover: Removing key: '{}' from map: '{}'...", key, mapName);
        IMap<Object, Object> map = getMap(mapName);
        map.remove(key);
    }

    public void clearMap(String mapName) {
        HzPersistenceModel persistenceModel = HzPersistenceModel.createMapPersistenceModel(mapName, null, null, HzPersistenceModel.ObjectOperation.DELETE);
        mapResilienceService.clearFromDB(persistenceModel);
        LOG.debug("Running failover: Clearing map '{}'...", mapName);
        IMap<Object, Object> map = getMap(mapName);
        map.clear();
    }
}
