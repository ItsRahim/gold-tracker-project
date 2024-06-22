package com.rahim.common.config.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.rahim.common.config.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties
public class HazelcastInstanceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastInstanceFactory.class);
    private final HazelcastClientProperties hazelcastClientProperties;
    private static final String FALLBACK_CLUSTER_NAME = "fallback-cluster";
    private static final String TEST_CLUSTER_NAME = "test-cluster";

    @Primary
    @Profile("!test")
    @Bean(name = "defaultHazelcastCluster")
    public HazelcastInstance hazelcastInstance() {
        try {
            hazelcastClientProperties.applyRetryConfig();
            return HazelcastClient.newHazelcastClient(hazelcastClientProperties);
        } catch (RuntimeException e) {
            LOG.error("Failed to create Hazelcast client instance, falling back to local instance: {}", e.getMessage());
            HealthStatus.setHzHealthy(false);
            return fallbackHazelcastInstance();
        }
    }

    @Profile("test")
    @Bean(name = "defaultHazelcastCluster")
    public HazelcastInstance testHazelcastInstance() {
        Config config = new Config();
        config.setClusterName(TEST_CLUSTER_NAME);
        return Hazelcast.newHazelcastInstance(config);
    }

    public HazelcastInstance fallbackHazelcastInstance() {
        Config config = new Config();
        config.setClusterName(FALLBACK_CLUSTER_NAME);
        return Hazelcast.newHazelcastInstance(config);
    }
}
