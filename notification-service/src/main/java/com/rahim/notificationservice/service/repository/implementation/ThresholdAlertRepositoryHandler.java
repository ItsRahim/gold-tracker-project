package com.rahim.notificationservice.service.repository.implementation;

import com.rahim.common.exception.EntityNotFoundException;
import com.rahim.notificationservice.entity.ThresholdAlert;
import com.rahim.notificationservice.repository.ThresholdAlertRepository;
import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThresholdAlertRepositoryHandler implements IThresholdAlertRepositoryHandler {

    private static final Logger log = LoggerFactory.getLogger(ThresholdAlertRepositoryHandler.class);
    private final ThresholdAlertRepository thresholdAlertRepository;

    @Override
    public ThresholdAlert findById(int alertId) {
        return thresholdAlertRepository.findById(alertId)
                .orElseThrow(() -> new EntityNotFoundException("Threshold does not exist for ID: " + alertId));
    }

    @Override
    public void deactivateAlert(int alertId) {
        try {
            ThresholdAlert thresholdAlert = findById(alertId);
            thresholdAlert.deactivate();
            thresholdAlertRepository.save(thresholdAlert);
            log.info("Deactivated alert with ID: {}", alertId);
        } catch (Exception e) {
            log.error("An error occurred while deactivating alert with ID: {}", alertId, e);
        }
    }

    @Override
    public void saveThresholdAlert(ThresholdAlert thresholdAlert) {
        if (ObjectUtils.isEmpty(thresholdAlert)) {
            log.error("ThresholdAlert is null or contains null properties. Unable to save.");
            throw new IllegalArgumentException("ThresholdAlert is null or contains null properties. Unable to save.");
        }
        try {
            thresholdAlertRepository.save(thresholdAlert);
        } catch (DataAccessException e) {
            log.error("Error saving ThresholdAlert to the database: {}", e.getMessage(), e);
            throw new DataIntegrityViolationException("Error saving Threshold Alert to the database");
        }
    }

    @Override
    public void deleteThresholdAlert(int alertId) {
        try {
            thresholdAlertRepository.deleteById(alertId);
            log.debug("ThresholdAlert with ID {} deleted successfully", alertId);
        } catch (Exception e) {
            log.error("An error occurred while deleting alert with ID: {}", alertId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThresholdAlert> getAllActiveAlerts() {
        return thresholdAlertRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public ThresholdAlert getAlertByAccountId(int accountId) {
        ThresholdAlert thresholdAlert = thresholdAlertRepository.findThresholdAlertByAccountId(accountId);
        if (thresholdAlert.getId() == null) {
            throw new EntityNotFoundException("Threshold alert not found for account ID: " + accountId);
        }

        return thresholdAlert;
    }
}
