package com.rahim.common.config.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.rahim.common.config.health.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class HazelcastMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastMonitor.class);
    private final HazelcastInstance hazelcastInstance;

    private static final long INITIAL_DELAY = 60000;
    private static final long HEARTBEAT_INTERVAL = 10000;
    private volatile boolean previousClusterHealth = true;

    public HazelcastMonitor(@Qualifier("defaultHazelcastCluster")HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Scheduled(initialDelay = INITIAL_DELAY, fixedRate = HEARTBEAT_INTERVAL)
    public void sendHeartbeat() {
        boolean isClusterHealthy = checkClusterHealth();
        if (isClusterHealthy != previousClusterHealth) {
            LOG.debug("Change in Hazelcast cluster status...");

            if (!isClusterHealthy) {
                handleUnhealthyCluster();
            } else {
                handleHealthyCluster();
            }

            previousClusterHealth = isClusterHealthy;
        }
    }

    private void handleUnhealthyCluster() {
        LOG.info("Unhealthy Hazelcast cluster detected. Using fallback cluster");
        HealthStatus.setHzHealthy(false);
    }

    private void handleHealthyCluster() {
        LOG.info("Healthy Hazelcast cluster detected");
        HealthStatus.setHzHealthy(true);
    }

    private boolean checkClusterHealth() {
        return !hazelcastInstance.getCluster().getMembers().isEmpty();
    }
}
