package com.rahim.userservice;

import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.service.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor
public class UserServiceApplication {
    private final IUserProfileService userProfileService;
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            userProfileService.generateEmailTokens(TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName(), 1, true, true);
        };
    }
}