package com.rahim.accountservice.config.hazelcast;

import com.hazelcast.collection.ISet;
import com.rahim.accountservice.service.hazelcast.CacheManager;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${hazelcast.sets.account-id}")
    String accountIdSet;

    @PostConstruct
    public void initialise() {
        LOG.debug("Initialising Hazelcast Storages...");
        List<Integer> activeNotifications = accountRepositoryHandler.getAccountActiveNotification();
        ISet<Integer> existingValues = hazelcastCacheManager.getSet(accountIdSet);

        activeNotifications.stream()
                .filter(accountId -> !existingValues.contains(accountId))
                .forEach(accountId -> hazelcastCacheManager.addToSet(accountId, accountIdSet));

        existingValues.stream()
                .filter(accountId -> !activeNotifications.contains(accountId))
                .forEach(accountId -> hazelcastCacheManager.removeFromSet(accountId, accountIdSet));
    }

}
