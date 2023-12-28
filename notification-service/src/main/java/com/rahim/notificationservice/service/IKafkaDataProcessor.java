package com.rahim.notificationservice.service;

public interface IKafkaDataProcessor {
    void processKafkaData(String priceData);
}
