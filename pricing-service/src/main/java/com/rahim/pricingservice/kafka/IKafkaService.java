package com.rahim.pricingservice.kafka;

public interface IKafkaService {
    void sendMessage(String topic, String data);
}
