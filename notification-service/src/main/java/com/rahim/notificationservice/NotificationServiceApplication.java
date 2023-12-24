package com.rahim.notificationservice;

import com.rahim.notificationservice.service.IThresholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor
public class NotificationServiceApplication {
    private final IThresholdService thresholdService;

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner () {
        return args -> thresholdService.processKafkaData("1600.98");
    }

}
