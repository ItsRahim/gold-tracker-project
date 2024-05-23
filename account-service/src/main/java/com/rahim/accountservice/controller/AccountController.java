package com.rahim.accountservice.controller;

import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.UserRequest;
import com.rahim.accountservice.service.account.AccountControllerService;
import com.rahim.accountservice.service.account.IAccountCreationService;
import com.rahim.accountservice.service.account.IAccountUpdateService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.rahim.accountservice.constant.AccountControllerEndpoint.*;

/**
 * @author Rahim Ahmed
 * @created 29/10/2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
@Tag(name = "Account Management", description = "Endpoints for managing user accounts")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);
    private final IAccountCreationService accountCreationService;
    private final IAccountUpdateService accountUpdateService;
    private final AccountControllerService accountControllerService;

    @Operation(summary = "Create new accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Accounts and Profiles created successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error Creating Accounts and Profiles", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createAccounts(@Parameter(description = "User account details", required = true) UserRequest userRequest) {
        return accountCreationService.createAccount(userRequest);
    }

    @Operation(summary = "Get account by account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error finding account", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = ACCOUNT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAccountById(@Parameter(description = "ID of the account to fetch", required = true) @PathVariable int accountId) {
        return accountControllerService.findAccountById(accountId);
    }

    @Operation(summary = "Update an existing account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error updating account", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping(value = ACCOUNT_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateAccount(
            @Parameter(description = "ID of the account to be updated", required = true) @PathVariable int accountId,
            @Parameter(description = "Map of updated account data", required = true) @RequestBody Map<String, String> updatedData) {
        return accountUpdateService.updateAccount(accountId, updatedData);
    }

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all accounts", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllAccounts() {
        return accountControllerService.getAllAccounts();
    }

    @Operation(summary = "Delete an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the account"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping(value = ACCOUNT_ID, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAccount(@Parameter(description = "ID of the account to be deleted", required = true) @PathVariable int accountId) {
        return accountControllerService.deleteAccount(accountId);
    }
}
