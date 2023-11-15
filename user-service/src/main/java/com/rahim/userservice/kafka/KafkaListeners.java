package com.rahim.userservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    private static final Logger log = LoggerFactory.getLogger(KafkaListeners.class);

    @KafkaListener(topics = "user-deletion-topic", groupId = "groupId")
    public void listen(String data) {
        log.info("Listening to information: " + data);
    }
}
