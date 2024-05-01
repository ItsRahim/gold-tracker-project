package com.rahim.notificationservice.service.repository;

import com.rahim.notificationservice.model.ThresholdAlert;

import java.util.List;
import java.util.Optional;

public interface IThresholdAlertRepositoryHandler {

    Optional<ThresholdAlert> findById(int alertId);
    void deactivateAlert(int alertId);
    void saveThresholdAlert(ThresholdAlert thresholdAlert);
    void deleteThresholdAlert(int alertId);
    List<ThresholdAlert> getAllActiveAlerts();
}
