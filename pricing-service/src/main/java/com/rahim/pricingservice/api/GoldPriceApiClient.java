package com.rahim.pricingservice.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "python-api", url = "${python-api.url}")
public interface GoldPriceApiClient {

    @GetMapping("/${python-api.source}")
    void getGoldPrice();

}
