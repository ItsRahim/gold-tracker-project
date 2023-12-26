package com.rahim.notificationservice.service;

import com.rahim.notificationservice.model.ThresholdAlert;

import java.util.Optional;

public interface IThresholdService {
    void processKafkaData(String priceData);

    Optional<ThresholdAlert> findById(int alertId);
}
