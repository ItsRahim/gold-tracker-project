package com.rahim.accountservice.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This is a utility class that contains Kafka topic constants which are resolved at runtime from Spring Cloud.
 */
@Getter
@Component
public final class TopicConstants {

    @Value("${topics.send-email-topic}")
    private String sendEmailTopic;

    @Value("${topics.send-id-result}")
    private String sendIdResult;

}
