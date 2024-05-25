package com.rahim.pricingservice.config;

import com.hazelcast.map.IMap;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HazelcastIntialiser {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastIntialiser.class);
    private final IGoldTypeRepositoryHandler goldTypeRepositoryHandler;
    private final CacheManager hazelcastCacheManager;

    private static final String GOLD_TYPE_ID_INITIALISED = "goldTypesInitialised";
    private IMap<String, Object> initialiserMap;

    @PostConstruct
    public void initialise() {
        initialiserMap = hazelcastCacheManager.getMap(HazelcastConstant.HAZELCAST_INITIALISER_MAP);
        initialiserMap.putIfAbsent(GOLD_TYPE_ID_INITIALISED, false);
        initialiseGoldTypeMap();
    }

    private void initialiseGoldTypeMap() {
        boolean isInitialised = (boolean) initialiserMap.get(GOLD_TYPE_ID_INITIALISED);
        if (isInitialised) {
            LOG.debug("Hazelcast map has already been initialised for gold types by another instance, skipping...");
            return;
        }
        
        LOG.debug("Initializing Hazelcast Storages for gold types...");
        List<Object[]> goldTypes = goldTypeRepositoryHandler.getGoldTypeNameAndId();

        goldTypes.forEach(goldType -> {
            String goldTypeName = (String) goldType[0];
            Integer goldTypeId = (Integer) goldType[1];
            hazelcastCacheManager.addToMap(HazelcastConstant.GOLD_TYPE_MAP, goldTypeName, goldTypeId);
            LOG.debug("Added gold type '{}' with ID '{}' to Hazelcast map.", goldTypeName, goldTypeId);
        });

        initialiserMap.put(GOLD_TYPE_ID_INITIALISED, true);
        LOG.debug("Hazelcast initialization for gold types complete.");
    }
}
