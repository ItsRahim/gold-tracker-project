package com.rahim.emailservice.service;

public interface IKafkaDataProcessor {
    void processKafkaData(String emailData);
}
