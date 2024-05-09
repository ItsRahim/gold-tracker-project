package com.rahim.common.config.hazelcast;

import com.rahim.common.config.health.HealthCheckAspect;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.common.service.hazelcast.HazelcastFailover;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class HazelcastMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastMonitor.class);
    private final CacheManager hazelcastCacheManager;
    private final HealthCheckAspect healthCheckAspect;
    private final HazelcastFailover hazelcastFailover;

    private static final long INITIAL_DELAY = 60000;
    private static final long HEARTBEAT_INTERVAL = 10000;
    private volatile boolean previousClusterHealth = false;

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
        LOG.info("Unhealthy Hazelcast cluster detected. Defaulting to Hazelcast failover cluster...");
        healthCheckAspect.setHzHealthy(false);
    }

    private void handleHealthyCluster() {
        LOG.info("Healthy Hazelcast cluster detected. Shutting down failover Hazelcast cluster");
        hazelcastFailover.shutdownInstance();
        healthCheckAspect.setHzHealthy(true);
        // TODO: add method to update hazelcast storages from DB
    }


    private boolean checkClusterHealth() {
        return !hazelcastCacheManager.getInstance().getCluster().getMembers().isEmpty();
    }
}
