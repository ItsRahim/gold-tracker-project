package com.rahim.pricingservice.controller;

import com.rahim.pricingservice.dto.GoldPriceDTO;
import com.rahim.pricingservice.service.IGoldPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 03/12/2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gold/pricing-service/gold-price")
public class GoldPriceController {

    private final IGoldPriceService goldPriceService;

    @GetMapping("/{goldId}")
    public ResponseEntity<GoldPriceDTO> getGoldPrice(@PathVariable int goldId) {
        Optional<GoldPriceDTO> goldPriceDTOOptional = goldPriceService.getGoldPrice(goldId);

        return goldPriceDTOOptional
                .map(goldPriceDTO -> ResponseEntity.status(HttpStatus.OK).body(goldPriceDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping()
    public ResponseEntity<List<GoldPriceDTO>> getAllGoldPrices() {
        List<GoldPriceDTO> goldPriceDTOList = goldPriceService.getAllGoldPrices();

        return ResponseEntity.status(HttpStatus.OK).body(goldPriceDTOList);
    }
}
