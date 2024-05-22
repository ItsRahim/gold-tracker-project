package com.rahim.notificationservice.service.repository.implementation;

import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.repository.ThresholdAlertRepository;
import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThresholdAlertRepositoryHandler implements IThresholdAlertRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ThresholdAlertRepositoryHandler.class);
    private final ThresholdAlertRepository thresholdAlertRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<ThresholdAlert> findById(int alertId) {
        try {
            return thresholdAlertRepository.findById(alertId);
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving alert with ID: {}", alertId, e);
            return Optional.empty();
        }
    }

    @Override
    public void deactivateAlert(int alertId) {
        try {
            findById(alertId).ifPresent(thresholdAlert -> {
                thresholdAlert.deactivate();
                thresholdAlertRepository.save(thresholdAlert);
            });
        } catch (Exception e) {
            LOG.error("An error occurred while deactivating alert with ID: {}", alertId, e);
        }
    }

    @Override
    public void saveThresholdAlert(ThresholdAlert thresholdAlert) {
        if (!ObjectUtils.anyNull(thresholdAlert)) {
            try {
                thresholdAlertRepository.save(thresholdAlert);
            } catch (DataAccessException e) {
                LOG.error("Error saving ThresholdAlert to the database", e);
                throw new DataIntegrityViolationException("Error saving Threshold Alert to the database", e);
            }
        } else {
            LOG.error("ThresholdAlert is null or contains null properties. Unable to save.");
            throw new IllegalArgumentException("ThresholdAlert is null or contains null properties. Unable to save.");
        }
    }

    @Override
    public void deleteThresholdAlert(int alertId) {
        findById(alertId).ifPresent(thresholdAlert -> {
            try {
                thresholdAlertRepository.deleteById(alertId);
                LOG.debug("ThresholdAlert with ID {} deleted successfully", alertId);
            } catch (Exception e) {
                LOG.warn("Attempted to delete non-existing ThresholdAlert with ID: {}", alertId);
                throw new EntityNotFoundException("ThresholdAlert with ID " + alertId + " not found");
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThresholdAlert> getAllActiveAlerts() {
        List<ThresholdAlert> activeAlerts = Collections.emptyList();

        try {
            activeAlerts = thresholdAlertRepository.findByIsActiveTrue();
            if (CollectionUtils.isEmpty(activeAlerts)) {
                LOG.debug("No active alerts found in the database");
            } else {
                LOG.debug("Fetched {} active alerts from the database", activeAlerts.size());
            }

        } catch (DataAccessException e) {
            LOG.error("Error occurred while fetching active alerts from the database", e);
        }

        return activeAlerts;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThresholdAlert> getAlertByAccountId(int accountId) {
        return thresholdAlertRepository.findThresholdAlertByAccountId(accountId);
    }

}
