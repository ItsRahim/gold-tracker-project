package com.rahim.pricingservice.config.hazelcast;

import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import com.rahim.accountservice.constant.HazelcastConstant;
import com.rahim.accountservice.service.hazelcast.CacheManager;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rahim Ahmed
 * @created 02/05/2024
 */
@Component
@RequiredArgsConstructor
public class HazelcastIntialiser {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastIntialiser.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final HazelcastConstant hazelcastConstant;
    private final CacheManager hazelcastCacheManager;

    private static final AtomicBoolean hasInitialised = new AtomicBoolean(false);
    private static final String HAZELCAST_INITIALISER_MAP = "hzInitStatusMap";
    private static final String ACTIVE_NOTIFICATION_ID_INITIALISED = "activeNotificationIdInitialised";
    private IMap<String, Boolean> initialiserMap;

    @PostConstruct
    public void initialise() {
        initialiserMap = hazelcastCacheManager.getMap(HAZELCAST_INITIALISER_MAP);
        initialiserMap.putIfAbsent(ACTIVE_NOTIFICATION_ID_INITIALISED, false);
        initialiseActiveNotification();
    }

    private void initialiseActiveNotification() {
        boolean isInitialised = initialiserMap.get(ACTIVE_NOTIFICATION_ID_INITIALISED);
        if (!isInitialised) {
            if (hasInitialised.compareAndSet(false, true)) {
                LOG.debug("Initialising Hazelcast Storages...");
                String accountIdSet = hazelcastConstant.getAccountIdSet();
                List<Integer> activeNotifications = accountRepositoryHandler.getAccountActiveNotification();
                ISet<Integer> existingNotifications = hazelcastCacheManager.getSet(accountIdSet);

                activeNotifications.stream()
                        .filter(accountId -> !existingNotifications.contains(accountId))
                        .forEach(accountId -> hazelcastCacheManager.addToSet(accountIdSet, accountId));

                existingNotifications.stream()
                        .filter(accountId -> !activeNotifications.contains(accountId))
                        .forEach(accountId -> hazelcastCacheManager.removeFromSet(accountIdSet, accountId));

                hazelcastCacheManager.addToMap(HAZELCAST_INITIALISER_MAP, ACTIVE_NOTIFICATION_ID_INITIALISED, true);
                LOG.debug("Hazelcast initialisation complete.");
            } else {
                LOG.debug("Hazelcast has already been initialised, skipping...");
            }
        } else {
            LOG.debug("Hazelcast has already been initialised by another instance, skipping...");
        }
    }
}
