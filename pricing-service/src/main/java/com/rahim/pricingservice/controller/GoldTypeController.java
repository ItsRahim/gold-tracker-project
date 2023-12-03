package com.rahim.pricingservice.controller;

import com.rahim.pricingservice.service.IGoldTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gold/pricing-service/gold-type")
public class GoldTypeController {
    private final IGoldTypeService goldTypeService;
}
