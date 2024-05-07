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
    private final HzCheckAspect hzCheckAspect;

    private static final long HEARTBEAT_INTERVAL = 1000;
    private volatile boolean previousClusterHealth = true;

    @Scheduled(fixedRate = HEARTBEAT_INTERVAL)
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
        LOG.warn("Unhealthy Hazelcast cluster detected. Defaulting to Database...");
        hzCheckAspect.setHealthy(false);
    }

    private void handleHealthyCluster() {
        LOG.debug("Healthy Hazelcast cluster detected");
        hzCheckAspect.setHealthy(true);
        // TODO: add method to update hazelcast storages from DB
    }


    private boolean checkClusterHealth() {
        return !hazelcastCacheManager.getInstance().getCluster().getMembers().isEmpty();
    }
}
