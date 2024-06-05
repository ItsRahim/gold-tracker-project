package com.rahim.common.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 05/06/2024
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaConsumerProperties {
    private String keyDeserializer;
    private String valueDeserializer;
}
