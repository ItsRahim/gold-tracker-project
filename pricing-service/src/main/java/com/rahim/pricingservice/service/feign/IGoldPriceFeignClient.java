package com.rahim.pricingservice.service.feign;

import com.rahim.pricingservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "gold-price-api-client", configuration = FeignConfig.class, url = "${gold-price-api.url}")
public interface IGoldPriceFeignClient {

    @GetMapping()
    String getGoldPrice();
}

