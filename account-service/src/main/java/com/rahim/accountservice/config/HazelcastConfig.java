package com.rahim.accountservice.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rahim Ahmed
 * @created 30/04/2024
 */
@Configuration
public class HazelcastConfig {

    @Value("${hazelcast.cluster.members}")
    private String clusterMembers;

    @Value("${hazelcast.cluster.name}")
    private String clusterName;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress(clusterMembers);
        clientConfig.setClusterName(clusterName);

        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
