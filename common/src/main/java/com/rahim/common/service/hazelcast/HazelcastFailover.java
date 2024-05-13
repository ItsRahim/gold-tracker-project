package com.rahim.common.service.hazelcast;

import com.hazelcast.collection.ISet;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.dao.HzMapDataAccess;
import com.rahim.common.dao.HzSetDataAccess;
import com.rahim.common.model.HzMapData;
import com.rahim.common.model.HzPersistenceModel;
import com.rahim.common.model.HzSetData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private final JdbcTemplate jdbcTemplate;

    private static final AtomicReference<HazelcastInstance> hazelcastInstanceRef = new AtomicReference<>();
    private static final AtomicBoolean isInitialised = new AtomicBoolean(false);
    private static final String INITIALISED_FLAG_KEY = "initialisedFlag";
    private static final String CLUSTER_NAME = "fallback-cluster";

    @Autowired
    public HazelcastFailover(@Qualifier("hzSetResilienceService") HzPersistenceService setResilienceService,
                             @Qualifier("hzMapResilienceService") HzPersistenceService mapResilienceService,
                             JdbcTemplate jdbcTemplate) {
        this.setResilienceService = setResilienceService;
        this.mapResilienceService = mapResilienceService;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Method to initialise a fallback local hazelcast cluster
     * @return The initialised HazelcastInstance.
     */
    private HazelcastInstance hazelcastInstance() {
        if (hazelcastInstanceRef.get() == null) {
            synchronized (HazelcastFailover.class) {
                if (hazelcastInstanceRef.get() == null) {
                    Config localConfig = new Config();
                    localConfig.setClusterName(CLUSTER_NAME);
                    hazelcastInstanceRef.set(Hazelcast.newHazelcastInstance(localConfig));

                    if (isInitialised.compareAndSet(false, true)) {
                        initialiseHazelcastData(hazelcastInstanceRef.get());
                    }
                }
            }
        }

        return hazelcastInstanceRef.get();
    }

    /**
     * Initialises Hazelcast data by loading maps and sets from the database if not already loaded.
     * @param instance The Hazelcast instance.
     */
    private void initialiseHazelcastData(HazelcastInstance instance) {
        IMap<String, Boolean> initialiserMap = instance.getMap(HazelcastConstant.HAZELCAST_INITIALISER_MAP);
        Boolean isDataLoaded = initialiserMap.putIfAbsent(INITIALISED_FLAG_KEY, false);

        if (isDataLoaded == null || !isDataLoaded) {
            LOG.debug("Loading maps and sets into Hazelcast from the database...");
            loadMaps();
            loadSets();
            initialiserMap.set(INITIALISED_FLAG_KEY, true);
            LOG.info("Maps and sets loaded into Hazelcast.");
        } else {
            LOG.debug("Fallback Hazelcast has already been initialised, skipping data loading.");
        }
    }

    /**
     * Loads map data from the database and adds it to Hazelcast.
     */
    private void loadMaps() {
        String query = "SELECT "
                + HzMapDataAccess.COL_MAP_NAME
                + ", "
                + HzMapDataAccess.COL_MAP_KEY
                + ", "
                + HzMapDataAccess.COL_MAP_VALUE
                + " FROM " + HzMapDataAccess.TABLE_NAME;

        try {
            List<HzMapData> mapDataList = jdbcTemplate.query(
                    query,
                    (resultSet, i) -> new HzMapData(
                            resultSet.getString(HzMapDataAccess.COL_MAP_NAME),
                            resultSet.getString(HzMapDataAccess.COL_MAP_KEY),
                            resultSet.getObject(HzMapDataAccess.COL_MAP_VALUE)
                    )
            );

            for (HzMapData mapData : mapDataList) {
                addToMap(mapData.getMapName(), mapData.getMapKey(), mapData.getMapValue());
            }

            LOG.debug("Successfully loaded maps into fallback hazelcast cluster from database");

        } catch (DataAccessException e) {
            LOG.error("An error occurred while accessing the database", e);
        }
    }

    /**
     * Loads set data from the database and adds it to Hazelcast.
     */
    private void loadSets() {
        String query = "SELECT "
                + HzSetDataAccess.COL_SET_NAME
                + ", "
                + HzSetDataAccess.COL_SET_VALUE
                + " FROM " + HzSetDataAccess.TABLE_NAME;

        try {
            List<HzSetData> setDataList = jdbcTemplate.query(
                    query,
                    (resultSet, i) -> new HzSetData(
                            resultSet.getString(HzSetDataAccess.COL_SET_NAME),
                            resultSet.getObject(HzSetDataAccess.COL_SET_VALUE)
                    )
            );

            for (HzSetData setData : setDataList) {
                addToSet(setData.getSetName(), setData.getSetValue());
            }

            LOG.debug("Successfully loaded sets into fallback hazelcast cluster from database");

        } catch (DataAccessException e) {
            LOG.error("An error occurred while accessing the database", e);
        }
    }

    /**
     * Shuts down fallback hazelcast cluster. Triggered upon healthy cluster being detected
     */
    public void shutdownInstance() {
        HazelcastInstance instance = hazelcastInstance();
        if (instance != null) {
            instance.shutdown();
            hazelcastInstanceRef.set(null);
            LOG.info("Shutdown failover cluster...");
        }
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
