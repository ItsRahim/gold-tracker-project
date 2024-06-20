package com.rahim.accountservice.controller;

import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.model.UserRequest;
import com.rahim.accountservice.request.account.AccountUpdateRequest;
import com.rahim.accountservice.service.account.IAccountCreationService;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.account.IAccountUpdateService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final IAccountCreationService accountCreationService;
    private final IAccountUpdateService accountUpdateService;
    private final IAccountDeletionService accountDeletionService;
    private final IAccountRepositoryHandler accountRepositoryHandler;

    @Operation(summary = "Create new accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account and Profile created successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad Request - Validation Error", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRequest> createAccounts(@Parameter(description = "User account details", required = true) @RequestBody UserRequest userRequest) {
        UserRequest account = accountCreationService.createAccount(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @Operation(summary = "Get account by account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error finding account", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = ACCOUNT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> findAccountById(@Parameter(description = "ID of the account to fetch", required = true) @PathVariable int accountId) {
        Account account = accountRepositoryHandler.findById(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @Operation(summary = "Update an existing account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error updating account", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping(value = ACCOUNT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateAccount(
            @Parameter(description = "ID of the account to be updated", required = true) @PathVariable int accountId,
            @Parameter(description = "Map of updated account data", required = true) @RequestBody AccountUpdateRequest updatedData) {
        Object response = accountUpdateService.updateAccount(accountId, updatedData);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all accounts", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllAccounts() {
        List<Account> accounts = accountRepositoryHandler.getAllAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @Operation(summary = "Delete an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the account"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping(value = ACCOUNT_ID, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAccount(@Parameter(description = "ID of the account to be deleted", required = true) @PathVariable int accountId) {
        boolean deletedRequested = accountDeletionService.requestAccountDelete(accountId);

        if (deletedRequested) {
            return ResponseEntity.status(HttpStatus.OK).body("Successfully requested deletion of account with ID: " + accountId);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account with ID " + accountId + " does not exist");
        }
    }
}
