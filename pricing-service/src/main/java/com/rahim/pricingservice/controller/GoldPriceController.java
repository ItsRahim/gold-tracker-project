package com.rahim.pricingservice.controller;

import com.rahim.pricingservice.dto.GoldPriceDTO;
import com.rahim.pricingservice.service.IGoldPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.rahim.pricingservice.constant.GoldPriceURLConstant.GOLD_ID;
import static com.rahim.pricingservice.constant.GoldPriceURLConstant.PRICE_BASE_URL;

/**
 * @author Rahim Ahmed
 * @created 03/12/2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(PRICE_BASE_URL)
public class GoldPriceController {

    private final IGoldPriceService goldPriceService;

    @GetMapping(GOLD_ID)
    public ResponseEntity<GoldPriceDTO> getGoldPrice(@PathVariable int goldPriceId) {
        Optional<GoldPriceDTO> goldPriceDTOOptional = goldPriceService.getGoldPrice(goldPriceId);

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
