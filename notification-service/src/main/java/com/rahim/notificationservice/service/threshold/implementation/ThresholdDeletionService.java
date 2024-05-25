package com.rahim.notificationservice.service.threshold.implementation;

import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.threshold.IThresholdDeletionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNotification(int alertId) {
        try {
            thresholdAlertRepositoryHandler.deleteThresholdAlert(alertId);
            LOG.info("Successfully deleted threshold alert with ID: {}", alertId);
            return true;
        } catch (Exception e) {
            LOG.error("An error has occurred attempting to delete threshold alert with ID: {}", alertId, e);
            return false;
        }
    }
}
