package com.rahim.investmentservice.controller;

import com.rahim.investmentservice.service.holding.HoldingDeletionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.rahim.investmentservice.constants.HoldingControllerEndpoint.*;

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Holding sold successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping(value = HOLDING_BY_ACCOUNT_AND_ID, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sellHolding(
            @Parameter(description = "The account id of account selling holding", required = true) @PathVariable int accountId,
            @Parameter(description = "The holding id being sold", required = true) @PathVariable int holdingId) {
        try {
            holdingDeletionService.sellHolding(accountId, holdingId);
            return ResponseEntity.status(HttpStatus.OK).body("Holding sold successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

}
