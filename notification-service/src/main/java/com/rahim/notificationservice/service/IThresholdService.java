package com.rahim.notificationservice.service;

import java.util.List;

public interface IThresholdService {
    List<String> processKafkaData(String priceData);
}
