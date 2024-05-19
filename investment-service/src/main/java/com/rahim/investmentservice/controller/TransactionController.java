package com.rahim.investmentservice.controller;

import com.rahim.investmentservice.dto.TxnRequestDto;
import com.rahim.investmentservice.service.transaction.TxnCreationService;
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

import static com.rahim.investmentservice.constants.TransactionControllerEndpoint.BASE_URL;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
@Tag(name = "Endpoint to manage user transactions")
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    private final TxnCreationService txnCreationService;

    @Operation(summary = "Create a new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping(value = "{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addNewTxn(
            @Parameter(description = "The ID of the account", required = true) @PathVariable Integer accountId,
            @Parameter(description = "The transaction details", required = true) @RequestBody TxnRequestDto txnRequestDto) {
        try {
            txnCreationService.addNewTransaction(accountId, txnRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully");
        } catch (IllegalStateException e) {
            LOG.error("An error occurred processing new transaction");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}
