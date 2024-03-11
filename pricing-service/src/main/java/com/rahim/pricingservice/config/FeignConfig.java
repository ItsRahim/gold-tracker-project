package com.rahim.pricingservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rahim Ahmed
 * @created 29/11/2023
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "gold-price-api")
public class FeignConfig {
    private String url;
}
