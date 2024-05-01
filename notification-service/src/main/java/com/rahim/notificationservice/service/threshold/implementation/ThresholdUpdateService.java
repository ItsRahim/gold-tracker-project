package com.rahim.notificationservice.service.threshold.implementation;

import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.threshold.IThresholdUpdateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
@Service
@RequiredArgsConstructor
public class ThresholdUpdateService implements IThresholdUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(ThresholdUpdateService.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;
    private static final String THRESHOLD_PRICE = "thresholdPrice";

    @Override
    public boolean updateNotification(int thresholdId, Map<String, String> updatedData) {
        Optional<ThresholdAlert> optionalAlert = thresholdAlertRepositoryHandler.findById(thresholdId);
        if (optionalAlert.isEmpty()) {
            LOG.warn("Alert with ID {} not found.", thresholdId);
            return false;
        }

        ThresholdAlert thresholdAlert = optionalAlert.get();
        if (!updateThresholdPrice(thresholdAlert, updatedData)) {
            LOG.info("Threshold alert with ID: {} was not updated.", thresholdId);
            return false;
        }

        try {
            thresholdAlertRepositoryHandler.saveThresholdAlert(thresholdAlert);
            LOG.info("Successfully updated threshold alert with ID: {}", thresholdId);
            return true;
        } catch (Exception e) {
            LOG.error("An error occurred while updating threshold alert with ID: {}", thresholdId, e);
            return false;
        }
    }

    private boolean updateThresholdPrice(ThresholdAlert thresholdAlert, Map<String, String> updatedData) {
        if (updatedData.containsKey(THRESHOLD_PRICE)) {
            BigDecimal originalPrice = thresholdAlert.getThresholdPrice();
            BigDecimal updatedPrice = new BigDecimal(updatedData.get(THRESHOLD_PRICE));
            if (!originalPrice.equals(updatedPrice)) {
                thresholdAlert.setThresholdPrice(updatedPrice);
                return true;
            } else {
                LOG.debug("Threshold alert already has the updated price: {}", updatedPrice);
                return false;
            }
        } else {
            LOG.warn("Updated data does not contain threshold price.");
            return false;
        }
    }

}
