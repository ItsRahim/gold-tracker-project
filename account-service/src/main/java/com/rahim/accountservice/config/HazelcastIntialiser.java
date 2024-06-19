package com.rahim.accountservice.config;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 02/05/2024
 */
@Component
@Profile("!test")
@RequiredArgsConstructor
public class HazelcastIntialiser {

    private static final Logger log = LoggerFactory.getLogger(HazelcastIntialiser.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final CacheManager hazelcastCacheManager;

    private static final String ACTIVE_NOTIFICATION_ID_INITIALISED = "activeNotificationIdInitialised";
    private static final String ACCOUNT_ID_INITIALISED = "accountIdInitialised";
    private IMap<String, Boolean> initialiserMap;

    @PostConstruct
    public void initialise() {
        log.debug("Initialising Account Service Hazelcast Storages...");
        initialiserMap = hazelcastCacheManager.getMap(HazelcastConstant.HAZELCAST_INITIALISER_MAP);
        initialiseActiveNotification();
        initialiseAccountIds();
    }

    private void initialiseActiveNotification() {
        boolean isInitialised = initialiserMap.getOrDefault(ACTIVE_NOTIFICATION_ID_INITIALISED, false);
        if (isInitialised) {
            log.debug("Active notification already initialised.");
            return;
        }

        log.debug("Initialising active notifications...");
        List<Integer> activeNotifications = accountRepositoryHandler.getAccountActiveNotification();
        ISet<Integer> existingNotifications = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET);

        activeNotifications.stream()
                .filter(accountId -> !existingNotifications.contains(accountId))
                .forEach(accountId -> {
                    hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, accountId);
                    log.debug("Added account {} to active notifications", accountId);
                });

        existingNotifications.stream()
                .filter(accountId -> !activeNotifications.contains(accountId))
                .forEach(accountId -> {
                    hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, accountId);
                    log.debug("Removed account {} from active notifications", accountId);
                });

        hazelcastCacheManager.addToMap(HazelcastConstant.HAZELCAST_INITIALISER_MAP, ACTIVE_NOTIFICATION_ID_INITIALISED, true);
        log.debug("Active notification initialisation complete.");
    }

    private void initialiseAccountIds() {
        boolean isInitialised = initialiserMap.getOrDefault(ACCOUNT_ID_INITIALISED, false);
        if (isInitialised) {
            log.debug("Account IDs already initialized.");
            return;
        }

        log.debug("Initializing account IDs...");
        List<Integer> accountIds = accountRepositoryHandler.getAllAccountIds();

        accountIds.forEach(accountId -> {
            hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_SET, accountId);
            log.debug("Added account {} to account ID set", accountId);
        });

        hazelcastCacheManager.addToMap(HazelcastConstant.HAZELCAST_INITIALISER_MAP, ACCOUNT_ID_INITIALISED, true);
        log.debug("Account ID initialisation complete.");
    }
}
