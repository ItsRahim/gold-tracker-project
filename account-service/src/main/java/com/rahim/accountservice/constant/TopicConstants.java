package com.rahim.accountservice.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This is a utility class that contains Kafka topic constants which are resolved at runtime from Spring Cloud.
 * It provides a centralized location for Kafka topics, which can improve maintainability if the topics change.
 * The topics are configurable and can be updated across all microservices, ensuring consistency and reducing hardcoding.
 * As a final class, it cannot be extended, ensuring that all Kafka topic constants are contained here.
 *
 */
@Getter
@Component
public final class TopicConstants {

    @Value("${topics.send-email-topic}")
    private String sendEmailTopic;

    @Value("${topics.send-id-result}")
    private String sendIdResult;

}
