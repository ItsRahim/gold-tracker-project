package com.rahim.common.config.hazelcast;

import com.rahim.common.service.hazelcast.CacheManager;
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

    private static final long HEARTBEAT_INTERVAL = 30000;
    private boolean previousClusterHealth = true;

    @Scheduled(fixedRate = HEARTBEAT_INTERVAL)
    public void sendHeartbeat() {
        LOG.debug("Checking Hazelcast cluster health");
        boolean isClusterHealthy = checkClusterHealth();
        if (isClusterHealthy != previousClusterHealth) {
            if (!isClusterHealthy) {
                LOG.warn("Unhealthy Hazelcast cluster detected. Defaulting to Database...");
                hazelcastCacheManager.setHealthy(false);
            } else {
                LOG.debug("Healthy Hazelcast cluster detected");
                hazelcastCacheManager.setHealthy(true);
                //TODO: add method to update hazelcast storages from DB
            }
            previousClusterHealth = isClusterHealthy;
        }
    }

    private boolean checkClusterHealth() {
        return !hazelcastCacheManager.getInstance().getCluster().getMembers().isEmpty();
    }
}
