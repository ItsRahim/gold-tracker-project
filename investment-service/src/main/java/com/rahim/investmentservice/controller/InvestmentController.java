package com.rahim.investmentservice.controller;

import com.rahim.investmentservice.entity.Investment;
import com.rahim.investmentservice.model.InvestmentResponse;
import com.rahim.investmentservice.request.InvestmentRequest;
import com.rahim.investmentservice.service.investment.InvestmentCreationService;
import com.rahim.investmentservice.service.repository.InvestmentRepositoryHandler;
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

import static com.rahim.investmentservice.constants.InvestmentControllerEndpoint.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
@Tag(name = "Endpoint to manage user investments")
public class InvestmentController {

    private static final Logger log = LoggerFactory.getLogger(InvestmentController.class);
    private final InvestmentCreationService investmentCreationService;
    private final InvestmentRepositoryHandler investmentRepositoryHandler;

    @Operation(summary = "Add a new investment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Investment created successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping(value = ACCOUNT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addNewInvestment(
            @Parameter(description = "The ID of the account", required = true) @PathVariable int accountId,
            @Parameter(description = "The new investment details", required = true) @RequestBody List<InvestmentRequest> investmentRequests) {
        for (InvestmentRequest investmentRequest : investmentRequests) {
            investmentCreationService.addNewInvestment(accountId, investmentRequest);
        }

        log.info("Investment created successfully for account ID: {}", accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Investment created successfully");
    }

    @Operation(summary = "Get an investment by account Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Investment for account id found successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Investment for account id not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "text/plain"))
    })
    @GetMapping(value = ACCOUNT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getInvestmentByAccountId(
            @Parameter(description = "The account id for investment to retrieve", required = true) @PathVariable int accountId) {
        try {
            log.info("Fetching investments for account with ID: {}", accountId);
            List<InvestmentResponse> investmentResponse = investmentRepositoryHandler.getInvestmentByAccountId(accountId);

            if (investmentResponse.isEmpty()) {
                log.warn("No investments found for account with ID: {}", accountId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No investments found for account with id " + accountId);
            }

            return ResponseEntity.status(HttpStatus.OK).body(investmentResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
