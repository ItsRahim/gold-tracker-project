package com.rahim.notificationservice.service.implementation;

import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.service.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.IThresholdService;
import com.rahim.notificationservice.service.IUserExistenceChecker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ThresholdService implements IThresholdService {
    private static final Logger LOG = LoggerFactory.getLogger(ThresholdService.class);
    private final IUserExistenceChecker userExistenceChecker;
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;

    @Override
    public void createNotification(ThresholdAlert thresholdAlert) {
        String userId = thresholdAlert.getId().toString();
        if (userExistenceChecker.checkUserExistence(userId, thresholdAlert)) {
            LOG.info("Attempting to add new threshold alert for user with ID: {}", thresholdAlert.getId());
            thresholdAlertRepositoryHandler.saveThresholdAlert(thresholdAlert);
        } else {
            LOG.error("Failed to create new threshold alert for user with ID: {}", thresholdAlert.getId());
        }
    }
    @Override
    public void updateNotification(Map<String, String> updatedData) {
    }

    @Override
    public void deleteNotification(int alertId) {
        thresholdAlertRepositoryHandler.deleteThresholdAlert(alertId);
    }
}
