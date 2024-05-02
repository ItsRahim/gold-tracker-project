package com.rahim.accountservice.config.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.YamlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Rahim Ahmed
 * @created 30/04/2024
 */
@Configuration
public class HazelcastConfig {

    @Value("${spring.hazelcast.config}")
    private String hazelcastConfigLocation;

    @Bean
    public HazelcastInstance hazelcastInstance() throws IOException {
        ClientConfig clientConfig = new YamlClientConfigBuilder(hazelcastConfigLocation).build();
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
