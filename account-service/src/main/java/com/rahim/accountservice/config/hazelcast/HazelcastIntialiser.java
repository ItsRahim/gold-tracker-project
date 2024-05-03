package com.rahim.accountservice.config.hazelcast;

import com.hazelcast.collection.ISet;
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

    @PostConstruct
    public void initialise() {
        initialiseActiveNotification();
    }

    private void initialiseActiveNotification() {
        if (hasInitialised.compareAndSet(false, true)) {
            LOG.debug("Initialising Hazelcast Storages...");
            String accountIdSet = hazelcastConstant.getAccountIdSet();
            List<Integer> activeNotifications = accountRepositoryHandler.getAccountActiveNotification();
            ISet<Integer> existingNotifications = hazelcastCacheManager.getSet(accountIdSet);

            activeNotifications.stream()
                    .filter(accountId -> !existingNotifications.contains(accountId))
                    .forEach(accountId -> hazelcastCacheManager.addToSet(accountId, accountIdSet));

            existingNotifications.stream()
                    .filter(accountId -> !activeNotifications.contains(accountId))
                    .forEach(accountId -> hazelcastCacheManager.removeFromSet(accountId, accountIdSet));

            LOG.debug("Hazelcast initialisation complete.");
        } else {
            LOG.debug("Hazelcast has already been initialised, skipping...");
        }
    }
}
