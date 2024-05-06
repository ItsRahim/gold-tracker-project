package com.rahim.common.config.hazelcast;

import com.rahim.common.service.hazelcast.ICacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 06/05/2024
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
public class HazelcastMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastMonitor.class);
    private final ICacheManager hazelcastCacheManager;

    private static final long HEARTBEAT_INTERVAL = 30000;

    @Scheduled(fixedRate = HEARTBEAT_INTERVAL)
    public void sendHeartbeat() {
        LOG.debug("Checking Hazelcast cluster health");
        boolean isClusterHealthy = checkClusterHealth();
        if (!isClusterHealthy) {
            LOG.warn("Unhealthy Hazelcast cluster detected. Defaulting to Database...");
            hazelcastCacheManager.setHealthy(false);
        } else {
            LOG.debug("Healthy Hazelcast cluster detected");
            hazelcastCacheManager.setHealthy(true);
        }
    }

    private boolean checkClusterHealth() {
        return !hazelcastCacheManager.getInstance().getCluster().getMembers().isEmpty();
    }
}
