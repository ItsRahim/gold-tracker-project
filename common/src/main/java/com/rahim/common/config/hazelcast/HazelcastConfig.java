package com.rahim.common.config.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ConnectionRetryConfig;
import com.hazelcast.client.config.YamlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HazelcastConfig {

    @Value("${spring.hazelcast.config}")
    private String hazelcastConfigLocation;

    @Bean
    public HazelcastInstance hazelcastInstance() throws IOException {
        ClientConfig clientConfig = new YamlClientConfigBuilder(hazelcastConfigLocation).build();
        customizeConnectionRetryConfig(clientConfig);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    private void customizeConnectionRetryConfig(ClientConfig clientConfig) {
        ConnectionRetryConfig connectionRetryConfig = clientConfig.getConnectionStrategyConfig().getConnectionRetryConfig();
        connectionRetryConfig.setClusterConnectTimeoutMillis(30000);
        connectionRetryConfig.setInitialBackoffMillis(1000);
        connectionRetryConfig.setMaxBackoffMillis(60000);
        connectionRetryConfig.setMultiplier(2);
        connectionRetryConfig.setJitter(0.1);
    }
}
