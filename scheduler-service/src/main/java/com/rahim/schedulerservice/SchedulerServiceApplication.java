package com.rahim.schedulerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.rahim.schedulerservice", "com.rahim.common"})
public class SchedulerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(SchedulerServiceApplication.class, args);
	}
}