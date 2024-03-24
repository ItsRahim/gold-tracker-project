package com.rahim.pricingservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "gold-price-api-client", url = "${gold-price-api.url}")
public interface IGoldPriceFeignClient {

    @GetMapping()
    String getGoldPrice();

}
