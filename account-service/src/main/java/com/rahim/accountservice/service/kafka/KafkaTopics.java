package com.rahim.accountservice.service.kafka;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 12/12/2023
 */
@Getter
@Component
public class KafkaTopics {

    private KafkaTopics() {}

    @Value("${topics.send-email-topic}")
    private String sendEmailTopic;

}
