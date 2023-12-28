package com.rahim.notificationservice.service;

import com.rahim.notificationservice.model.ThresholdAlert;

import java.util.Map;

public interface IThresholdService {
    void createNotification(ThresholdAlert thresholdAlert);
    void updateNotification(Map<String, String> updatedData);
    void deleteNotification(int alertId);
}
