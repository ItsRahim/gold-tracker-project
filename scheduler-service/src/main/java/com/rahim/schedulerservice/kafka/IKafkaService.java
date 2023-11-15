package com.rahim.schedulerservice.kafka;

public interface IKafkaService {
    void sendMessage(String topic, String data);
}
