package com.rahim.pricingservice.controller;

import com.rahim.pricingservice.model.GoldPriceDTO;
import com.rahim.pricingservice.service.repository.implementation.GoldPriceRepositoryHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.rahim.pricingservice.constant.GoldPriceURLConstant.*;

/**
 * @author Rahim Ahmed
 * @created 03/12/2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(PRICE_BASE_URL)
@Tag(name = "Gold Price Management", description = "Endpoints for managing gold prices")
public class GoldPriceController {

    private static final Logger log = LoggerFactory.getLogger(GoldPriceController.class);
    private final GoldPriceRepositoryHandler goldPriceRepositoryHandler;

    @Operation(summary = "Get gold price by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gold price found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GoldPriceDTO.class))),
            @ApiResponse(responseCode = "404", description = "Gold price not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error retrieving gold price", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = GOLD_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoldPriceDTO> getGoldPrice(
            @Parameter(description = "ID of gold price to be fetched") @PathVariable int goldPriceId) {
        GoldPriceDTO goldPriceDto = goldPriceRepositoryHandler.getGoldPrice(goldPriceId);
        return ResponseEntity.status(HttpStatus.OK).body(goldPriceDto);
    }

    @Operation(summary = "Get all gold prices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gold prices retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GoldPriceDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error retrieving gold prices", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoldPriceDTO>> getAllGoldPrices() {
        List<GoldPriceDTO> goldPrices = goldPriceRepositoryHandler.getAllGoldPrices();
        return ResponseEntity.status(HttpStatus.OK).body(goldPrices);
    }

}
