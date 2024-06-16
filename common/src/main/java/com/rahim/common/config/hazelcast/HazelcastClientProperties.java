package com.rahim.common.config.hazelcast;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
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

    public void applyRetryConfig() {
        ClientConnectionStrategyConfig connectionStrategyConfig = getConnectionStrategyConfig();

        if (connectionStrategyConfig == null) {
            connectionStrategyConfig = new ClientConnectionStrategyConfig();
            setConnectionStrategyConfig(connectionStrategyConfig);
        }

        connectionStrategyConfig.getConnectionRetryConfig().setClusterConnectTimeoutMillis(30000);
        connectionStrategyConfig.getConnectionRetryConfig().setInitialBackoffMillis(1000);
        connectionStrategyConfig.getConnectionRetryConfig().setMaxBackoffMillis(60000);
        connectionStrategyConfig.getConnectionRetryConfig().setMultiplier(1.5);
        connectionStrategyConfig.getConnectionRetryConfig().setJitter(0.2);
    }
}
