package com.rahim.notificationservice.service;

import com.rahim.notificationservice.model.ThresholdAlert;

import java.util.Optional;

public interface IThresholdAlertRepositoryHandler {
    Optional<ThresholdAlert> findById(int alertId);
    void deactivateAlert(int alertId);
    void saveThresholdAlert(ThresholdAlert thresholdAlert);
    void deleteThresholdAlert(int alertId);
}
