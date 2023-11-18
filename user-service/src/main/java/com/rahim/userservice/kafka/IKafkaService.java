package com.rahim.userservice.kafka;

public interface IKafkaService {
    void sendMessage(String topic, String data);
}
