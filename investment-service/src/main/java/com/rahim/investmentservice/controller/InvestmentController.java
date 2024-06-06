package com.rahim.investmentservice.controller;

import com.rahim.investmentservice.request.InvestmentRequest;
import com.rahim.investmentservice.service.investment.InvestmentCreationService;
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

    @Operation(summary = "Add a new investment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Investment created successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping(value = ACCOUNT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addNewInvestment(
            @Parameter(description = "The ID of the account", required = true) @PathVariable Integer accountId,
            @Parameter(description = "The new investment details", required = true) @RequestBody List<InvestmentRequest> investmentRequests) {
        for (InvestmentRequest investmentRequest : investmentRequests) {
            investmentCreationService.addNewInvestment(accountId, investmentRequest);
        }

        log.info("Investment created successfully for account ID: {}", accountId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Investment created successfully");
    }

}
