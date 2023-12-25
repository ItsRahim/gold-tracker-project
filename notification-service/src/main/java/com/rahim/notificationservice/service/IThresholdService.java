package com.rahim.notificationservice.service;

import com.rahim.notificationservice.model.ThresholdAlert;

import java.util.List;
import java.util.Optional;

public interface IThresholdService {
    List<String> processKafkaData(String priceData);

    Optional<ThresholdAlert> findById(int alertId);
}
