package com.rahim.notificationservice.service.implementation;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.service.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.IThresholdService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThresholdService implements IThresholdService {

    private static final Logger LOG = LoggerFactory.getLogger(ThresholdService.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;
    private final HazelcastInstance hazelcastInstance;
    private static final String THRESHOLD_PRICE = "thresholdPrice";

    @Override
    public void createNotification(ThresholdAlert thresholdAlert) {
        int accountId = thresholdAlert.getAccountId();
        if (accountExists(accountId)) {
            thresholdAlertRepositoryHandler.saveThresholdAlert(thresholdAlert);
            LOG.info("Successfully added threshold alert for user with ID: {}", accountId);
        } else {
            LOG.warn("Failed to create new threshold alert for user with ID: {}. Account invalid/notifications not enabled on account", accountId);
        }
    }

    private boolean accountExists(int userId) {
        ISet<Integer> userIds = hazelcastInstance.getSet("accountNotificationSet");
        return userIds.contains(userId);
    }

    @Override
    public void updateNotification(Map<String, String> updatedData, int alertId) {
        Optional<ThresholdAlert> optionalAlert = thresholdAlertRepositoryHandler.findById(alertId);
        optionalAlert.ifPresent(thresholdAlert -> {
            if (updatedData.containsKey(THRESHOLD_PRICE)) {
                BigDecimal price = new BigDecimal(updatedData.get(THRESHOLD_PRICE));
                thresholdAlert.setThresholdPrice(price);
            }
            try {
                thresholdAlertRepositoryHandler.saveThresholdAlert(thresholdAlert);
                LOG.info("Successfully updated threshold alert with ID: {}", alertId);
            } catch (Exception e) {
                LOG.error("An error occurred while updating threshold alert with ID: {}", alertId, e);
            }
        });

        if (optionalAlert.isEmpty()) {
            LOG.warn("Alert with ID {} not found.", alertId);
        }
    }

    @Override
    public void deleteNotification(int alertId) {
        Optional<ThresholdAlert> optionalAlert = thresholdAlertRepositoryHandler.findById(alertId);
        optionalAlert.ifPresent(alert -> {
            try {
                thresholdAlertRepositoryHandler.deleteThresholdAlert(alert.getId());
                LOG.info("Successfully deleted threshold alert with ID: {}", alertId);
            } catch (Exception e) {
                LOG.error("An error has occurred attempting to delete threshold alert with ID: {}", alertId);
            }
        });

        if (optionalAlert.isEmpty()) {
            LOG.warn("Alert not deleted with ID {}. Not found.", alertId);
        }
    }
}
