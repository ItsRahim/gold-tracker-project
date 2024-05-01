package com.rahim.notificationservice.service.threshold.implementation;

import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.threshold.IThresholdDeletionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
@Service
@RequiredArgsConstructor
public class ThresholdDeletionService implements IThresholdDeletionService {

    private static final Logger LOG = LoggerFactory.getLogger(ThresholdDeletionService.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;

    @Override
    public boolean deleteNotification(int alertId) {
        Optional<ThresholdAlert> optionalAlert = thresholdAlertRepositoryHandler.findById(alertId);
        if (optionalAlert.isPresent()) {
            try {
                thresholdAlertRepositoryHandler.deleteThresholdAlert(alertId);
                LOG.info("Successfully deleted threshold alert with ID: {}", alertId);
                return true;
            } catch (Exception e) {
                LOG.error("An error has occurred attempting to delete threshold alert with ID: {}", alertId, e);
                return false;
            }
        } else {
            LOG.warn("Threshold alert with ID {} not found", alertId);
            return false;
        }
    }

}
