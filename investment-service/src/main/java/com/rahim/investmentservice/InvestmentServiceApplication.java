package com.rahim.investmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.rahim.investmentservice", "com.rahim.common"})
public class InvestmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvestmentServiceApplication.class, args);
    }

}
