package com.rahim.common.config.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ConnectionRetryConfig;
import com.hazelcast.client.config.YamlClientConfigBuilder;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.rahim.common.config.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class HazelcastInstanceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastInstanceFactory.class);
    private static final String FALLBACK_CLUSTER_NAME = "fallback-cluster";

    @Value("${spring.hazelcast.config}")
    private String hazelcastConfigLocation;

    @Primary
    @Bean(name = "defaultHazelcastCluster")
    public HazelcastInstance hazelcastInstance() {
        try {
            ClientConfig clientConfig = new YamlClientConfigBuilder(hazelcastConfigLocation).build();
            setConnectionRetryConfig(clientConfig);
            return HazelcastClient.newHazelcastClient(clientConfig);
        } catch (IOException | RuntimeException e) {
            LOG.error("Failed to create Hazelcast client instance, falling back to local instance: {}", e.getMessage());
            HealthStatus.setHzHealthy(false);
            return fallbackHazelcastInstance();
        }
    }

    @Bean(name = "fallbackHazelcastCluster")
    public HazelcastInstance fallbackHazelcastInstance() {
        Config config = new Config();
        config.setClusterName(FALLBACK_CLUSTER_NAME);
        return Hazelcast.newHazelcastInstance(config);
    }

    private void setConnectionRetryConfig(ClientConfig clientConfig) {
        ConnectionRetryConfig connectionRetryConfig = clientConfig.getConnectionStrategyConfig().getConnectionRetryConfig();
        connectionRetryConfig.setClusterConnectTimeoutMillis(30000);
        connectionRetryConfig.setInitialBackoffMillis(1000);
        connectionRetryConfig.setMaxBackoffMillis(60000);
        connectionRetryConfig.setMultiplier(2);
        connectionRetryConfig.setJitter(0.1);
    }
}
