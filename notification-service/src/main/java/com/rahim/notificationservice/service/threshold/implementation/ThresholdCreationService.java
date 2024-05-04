package com.rahim.notificationservice.service.threshold.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.service.hazelcast.CacheManager;
import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.threshold.IThresholdCreationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
@Service
@RequiredArgsConstructor
public class ThresholdCreationService implements IThresholdCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(ThresholdCreationService.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;
    private final CacheManager hazelcastCacheManager;

    @Value("${hazelcast.sets.account-id}")
    String accountIdSet;

    @Override
    public boolean createNotification(ThresholdAlert thresholdAlert) {
        int accountId = thresholdAlert.getAccountId();
        if (accountExists(accountId)) {
            thresholdAlertRepositoryHandler.saveThresholdAlert(thresholdAlert);
            LOG.debug("Successfully added threshold alert for user with ID: {}", accountId);
            return true;
        } else {
            LOG.warn("Failed to create new threshold alert for user with ID: {}. Account invalid/notifications not enabled on account", accountId);
            return false;
        }
    }

    private boolean accountExists(int accountId) {
        LOG.debug("Searching Hazelcast set for account id");
        ISet<Integer> accountIds = hazelcastCacheManager.getSet(accountIdSet);
        return accountIds.contains(accountId);
    }
}
