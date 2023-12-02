package com.rahim.pricingservice.config;

import com.rahim.pricingservice.util.GoldPriceCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GoldPriceCalculator goldPriceCalculator() {
        return new GoldPriceCalculator();
    }
}
