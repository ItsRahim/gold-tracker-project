package com.rahim.notificationservice.kafka;

public interface IKafkaService {
    void sendMessage(String topic, String data);
}
