package com.rahim.common.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * @author Rahim Ahmed
 * @created 11/06/2024
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private String securityProtocol;
    private String keystoreFile;
    private String keystorePassword;
    private String truststoreFile;
    private String truststorePassword;
}
