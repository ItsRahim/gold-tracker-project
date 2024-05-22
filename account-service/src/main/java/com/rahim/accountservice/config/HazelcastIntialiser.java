package com.rahim.accountservice.config;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.common.service.hazelcast.implementation.HazelcastCacheManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 02/05/2024
 */
@Component
@RequiredArgsConstructor
public class HazelcastIntialiser {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastIntialiser.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final CacheManager hazelcastCacheManager;

    private static final String ACTIVE_NOTIFICATION_ID_INITIALISED = "activeNotificationIdInitialised";
    private static final String ACCOUNT_ID_INITIALISED = "accountIdInitialised";
    private IMap<String, Boolean> initialiserMap;

    @PostConstruct
    public void initialise() {
        LOG.debug("Initializing Account Service Hazelcast Storages...");
        initialiserMap = hazelcastCacheManager.getMap(HazelcastConstant.HAZELCAST_INITIALISER_MAP);
        initialiseActiveNotification();
        initialiseAccountIds();
    }

    private void initialiseActiveNotification() {
        boolean isInitialised = initialiserMap.getOrDefault(ACTIVE_NOTIFICATION_ID_INITIALISED, false);
        if (!isInitialised) {
            LOG.debug("Initializing active notifications...");
            List<Integer> activeNotifications = accountRepositoryHandler.getAccountActiveNotification();
            ISet<Integer> existingNotifications = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET);

            activeNotifications.stream()
                    .filter(accountId -> !existingNotifications.contains(accountId))
                    .forEach(accountId -> {
                        hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, accountId);
                        LOG.debug("Added account {} to active notifications", accountId);
                    });

            existingNotifications.stream()
                    .filter(accountId -> !activeNotifications.contains(accountId))
                    .forEach(accountId -> {
                        hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, accountId);
                        LOG.debug("Removed account {} from active notifications", accountId);
                    });

            hazelcastCacheManager.addToMap(HazelcastConstant.HAZELCAST_INITIALISER_MAP, ACTIVE_NOTIFICATION_ID_INITIALISED, true);
            LOG.debug("Active notification initialization complete.");
        } else {
            LOG.debug("Active notification already initialized.");
        }
    }

    private void initialiseAccountIds() {
        boolean isInitialised = initialiserMap.getOrDefault(ACCOUNT_ID_INITIALISED, false);
        if (!isInitialised) {
            LOG.debug("Initializing account IDs...");
            List<Integer> accountIds = accountRepositoryHandler.getAllAccountIds();

            accountIds.forEach(accountId -> {
                hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_SET, accountId);
                LOG.debug("Added account {} to account ID set", accountId);
            });

            hazelcastCacheManager.addToMap(HazelcastConstant.HAZELCAST_INITIALISER_MAP, ACCOUNT_ID_INITIALISED, true);
            LOG.debug("Account ID initialization complete.");
        } else {
            LOG.debug("Account IDs already initialized.");
        }
    }
}
