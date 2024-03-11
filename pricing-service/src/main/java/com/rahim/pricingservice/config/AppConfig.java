package com.rahim.pricingservice.config;

import com.rahim.pricingservice.util.GoldPriceCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rahim Ahmed
 * @created 02/12/2023
 */
@Configuration
public class AppConfig {

    @Bean
    public GoldPriceCalculator goldPriceCalculator() {
        return new GoldPriceCalculator();
    }
}
