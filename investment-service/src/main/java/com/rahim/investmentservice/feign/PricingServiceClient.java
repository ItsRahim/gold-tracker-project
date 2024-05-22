package com.rahim.investmentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Rahim Ahmed
 * @created 22/05/2024
 */
@FeignClient(name = "PRICING-SERVICE")
public interface PricingServiceClient {

    @GetMapping("/api/v1/gold/price/{goldTypeId}")
    ResponseEntity<String> getGoldPrice(@PathVariable("goldTypeId") int goldTypeId);

}
