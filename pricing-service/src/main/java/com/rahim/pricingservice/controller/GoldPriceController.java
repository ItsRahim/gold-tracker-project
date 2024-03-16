package com.rahim.pricingservice.controller;

import com.rahim.pricingservice.dto.GoldPriceDTO;
import com.rahim.pricingservice.service.repository.implementation.GoldPriceRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceController.class);

    private final GoldPriceRepositoryHandler goldPriceRepositoryHandler;

    @GetMapping(GOLD_ID)
    public ResponseEntity<GoldPriceDTO> getGoldPrice(@PathVariable int goldPriceId) {
        try {
            Optional<GoldPriceDTO> goldPriceDTOOptional = goldPriceRepositoryHandler.getGoldPrice(goldPriceId);

            return goldPriceDTOOptional
                    .map(goldPriceDTO -> ResponseEntity.status(HttpStatus.OK).body(goldPriceDTO))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (RuntimeException e) {
            LOG.error("Error retrieving gold price with ID {}: {}", goldPriceId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping()
    public ResponseEntity<List<GoldPriceDTO>> getAllGoldPrices() {
        List<GoldPriceDTO> goldPrices = goldPriceRepositoryHandler.getAllGoldPrices();

        return ResponseEntity.status(HttpStatus.OK).body(goldPrices);
    }

}
