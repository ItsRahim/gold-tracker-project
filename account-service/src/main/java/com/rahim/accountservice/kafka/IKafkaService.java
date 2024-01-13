package com.rahim.accountservice.kafka;

public interface IKafkaService {
    void sendMessage(String topic, String data);
}
