package com.rahim.investmentservice.controller;

import com.rahim.investmentservice.entity.Holding;
import com.rahim.investmentservice.service.holding.HoldingDeletionService;
import com.rahim.investmentservice.service.repository.HoldingRepositoryHandler;
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

import java.util.List;

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

    private static final Logger log = LoggerFactory.getLogger(HoldingController.class);
    private final HoldingDeletionService holdingDeletionService;
    private final HoldingRepositoryHandler holdingRepositoryHandler;

    @Operation(summary = "Sell holdings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Holdings sold successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Holdings not found. Unable to sell", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "text/plain"))
    })
    @DeleteMapping(value = ACCOUNT_ID, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> sellHoldings(
            @Parameter(description = "The account id of account selling holdings", required = true) @PathVariable int accountId,
            @Parameter(description = "List of holding ids being sold", required = true) @RequestBody List<Integer> holdingIds) {
        try {
            List<Integer> failedHoldingIds = holdingDeletionService.sellHolding(holdingIds, accountId);
            if (!failedHoldingIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Holdings could not be sold for account with ID " + accountId + ". Some holdings do not exist for this account: " + failedHoldingIds);
            }
            return ResponseEntity.status(HttpStatus.OK).body("All holdings sold successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @Operation(summary = "Get all holdings by account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Holdings for account id found successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Holdings for account id not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping(value = ACCOUNT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getHoldingByAccountId(
            @Parameter(description = "The account id to get all holdings for", required = true) @PathVariable int accountId) {
        try {
            List<Holding> allHoldings = holdingRepositoryHandler.getHoldingsByAccountId(accountId);
            if (allHoldings.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No holdings found for account with ID " + accountId);
            }
            return ResponseEntity.status(HttpStatus.OK).body(allHoldings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}

