package com.rahim.notificationservice.service.implementation;

import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.service.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.IThresholdService;
import com.rahim.notificationservice.service.IUserExistenceChecker;
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
    public void updateNotification(Map<String, String> updatedData, int alertId) {
        Optional<ThresholdAlert> optionalAlert = thresholdAlertRepositoryHandler.findById(alertId);
        if(optionalAlert.isPresent()) {
            ThresholdAlert thresholdAlert = optionalAlert.get();
            try {
                if(updatedData.containsKey("thresholdPrice")) {
                    BigDecimal price = new BigDecimal(updatedData.get("thresholdPrice"));
                    thresholdAlert.setThresholdPrice(price);
                }

                thresholdAlertRepositoryHandler.saveThresholdAlert(thresholdAlert);

                LOG.info("Successfully updated threshold alert with ID: {}", alertId);
            } catch (Exception e) {
                LOG.error("An error has occurred attempting to updated threshold alert with ID: {}", alertId);
                throw new RuntimeException(e);
            }
        } else {
            LOG.warn("Alert with ID {} not found.", alertId);
            throw new RuntimeException("Alert not found.");
        }
    }

    @Override
    public void deleteNotification(int alertId) {
        Optional<ThresholdAlert> optionalAlert = thresholdAlertRepositoryHandler.findById(alertId);
        if(optionalAlert.isPresent()) {
            try {
                thresholdAlertRepositoryHandler.deleteThresholdAlert(alertId);

                LOG.info("Successfully deleted threshold alert with ID: {}", alertId);
            } catch (Exception e) {
                LOG.error("An error has occurred attempting to delete threshold alert with ID: {}", alertId);
                throw new RuntimeException(e);
            }
        } else {
            LOG.warn("Alert with ID {} not found.", alertId);
            throw new RuntimeException("Alert not found.");
        }
    }
}
