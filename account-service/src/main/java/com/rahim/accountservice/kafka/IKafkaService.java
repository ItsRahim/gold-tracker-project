package com.rahim.accountservice.kafka;

/**
 * @author Rahim Ahmed
 * @created 18/11/2023
 */
public interface IKafkaService {
    void sendMessage(String topic, String data);
}
