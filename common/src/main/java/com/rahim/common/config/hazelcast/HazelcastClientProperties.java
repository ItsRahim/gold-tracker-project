package com.rahim.common.config.hazelcast;

import com.hazelcast.client.config.ClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @author Rahim Ahmed
 * @created 16/06/2024
 */
@Validated
@Configuration
@ConfigurationProperties(prefix = "hazelcast")
public class HazelcastClientProperties extends ClientConfig {
}
