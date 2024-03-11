package com.rahim.pricingservice.kafka;

/**
 * @author Rahim Ahmed
 * @created 03/12/2023
 */
public interface IKafkaService {
    void sendMessage(String topic, String data);
}
