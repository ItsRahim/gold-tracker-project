package com.rahim.investmentservice.controller;

import com.rahim.investmentservice.service.holding.HoldingDeletionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.rahim.investmentservice.constants.HoldingControllerEndpoint.BASE_URL;
import static com.rahim.investmentservice.constants.HoldingControllerEndpoint.HOLDING_ID;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
@Tag(name = "Endpoint to manage user holdings")
public class HoldingController {

    private static final Logger LOG = LoggerFactory.getLogger(HoldingController.class);
    private final HoldingDeletionService holdingDeletionService;

    @Operation(summary = "Sell a holding")
    @DeleteMapping(value = HOLDING_ID, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sellHolding(
            @Parameter(description = "The holding id being sold", required = true) @PathVariable int holdingId) {
        try {
            holdingDeletionService.sellHolding(holdingId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
