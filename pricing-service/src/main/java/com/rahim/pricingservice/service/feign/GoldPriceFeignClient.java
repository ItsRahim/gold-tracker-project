package com.rahim.pricingservice.service.feign;

import com.rahim.pricingservice.config.GoldPriceFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "gold-price-api-client", configuration = GoldPriceFeignClientConfig.class)
public interface GoldPriceFeignClient {

    @GetMapping("/api/v1/gold")
    String getGoldPrice();
}

