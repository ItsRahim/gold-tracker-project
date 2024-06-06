package com.rahim.notificationservice.service.threshold.implementation;

import com.rahim.notificationservice.entity.ThresholdAlert;
import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.threshold.IThresholdUpdateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
@Service
@RequiredArgsConstructor
public class ThresholdUpdateService implements IThresholdUpdateService {

    private static final Logger log = LoggerFactory.getLogger(ThresholdUpdateService.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;
    private static final String THRESHOLD_PRICE = "thresholdPrice";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotification(int thresholdId, Map<String, String> updatedData) {
        ThresholdAlert thresholdAlert = thresholdAlertRepositoryHandler.findById(thresholdId);

        if (!updateThresholdPrice(thresholdAlert, updatedData)) {
            log.info("Threshold alert with ID: {} was not updated.", thresholdId);
            return;
        }

        try {
            thresholdAlertRepositoryHandler.saveThresholdAlert(thresholdAlert);
            log.info("Successfully updated threshold alert with ID: {}", thresholdId);
        } catch (Exception e) {
            log.error("An error occurred while updating threshold alert with ID: {}", thresholdId, e);
        }
    }

    private boolean updateThresholdPrice(ThresholdAlert thresholdAlert, Map<String, String> updatedData) {
        if (!updatedData.containsKey(THRESHOLD_PRICE)) {
            log.warn("Updated data does not contain threshold price.");
            return false;
        }

        BigDecimal originalPrice = thresholdAlert.getThresholdPrice();
        BigDecimal updatedPrice = new BigDecimal(updatedData.get(THRESHOLD_PRICE));
        if (originalPrice.equals(updatedPrice)) {
            log.debug("Threshold alert already has the updated price: {}", updatedPrice);
            return false;
        }

        thresholdAlert.setThresholdPrice(updatedPrice);
        return true;
    }
}
