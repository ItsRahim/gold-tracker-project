package com.rahim.pricingservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "python-api", url = "${python-api.url}")
public interface IGoldPriceFeignClient {

    @GetMapping("/${python-api.source}")
    String getGoldPrice();

}
