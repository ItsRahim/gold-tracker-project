package com.rahim.notificationservice.service.repository;

import com.rahim.notificationservice.model.ThresholdAlert;

import java.util.List;

public interface IThresholdAlertRepositoryHandler {

    ThresholdAlert findById(int alertId);
    void deactivateAlert(int alertId);
    void saveThresholdAlert(ThresholdAlert thresholdAlert);
    void deleteThresholdAlert(int alertId);
    List<ThresholdAlert> getAllActiveAlerts();
    ThresholdAlert getAlertByAccountId(int accountId);
}
