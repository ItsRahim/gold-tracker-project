package com.rahim.accountservice.controller;

import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.UserRequest;
import com.rahim.accountservice.service.account.IAccountCreationService;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.account.IAccountUpdateService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.Map;
import java.util.Optional;

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
    private final IAccountDeletionService accountDeletionService;
    private final IAccountUpdateService accountUpdateService;
    private final IAccountRepositoryHandler accountRepositoryHandler;

    @Operation(summary = "Create new accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Accounts and Profiles created successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error Creating Accounts and Profiles", content = @Content(mediaType = "text/plain"))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createAccounts(@RequestBody List<UserRequest> userRequests) {
        try {
            for (UserRequest userRequest : userRequests) {
                accountCreationService.createAccount(userRequest);
                LOG.info("Successfully Created Account: {}", userRequest.getProfile().getUsername());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Accounts and Profiles created successfully");
        } catch (Exception e) {
            LOG.error("Error creating Accounts and Profiles: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Creating Accounts and Profiles");
        }
    }

    @Operation(summary = "Get account by account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error finding account", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = ACCOUNT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAccountById(@PathVariable int accountId) {
        try {
            Optional<Account> accountOptional = accountRepositoryHandler.findById(accountId);

            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                LOG.info("Account found with ID: {}", accountId);
                return ResponseEntity.status(HttpStatus.OK).body(account);
            } else {
                LOG.info("Account not found with ID: {}", accountId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
            }
        } catch (Exception e) {
            LOG.error("Error finding user with ID: {}", accountId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user");
        }
    }


    @Operation(summary = "Update an existing account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error updating account", content = @Content(mediaType = "text/plain"))
    })
    @PutMapping(value = ACCOUNT_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateAccount(@PathVariable int accountId, @RequestBody Map<String, String> updatedData) {
        try {
            boolean hasUpdated = accountUpdateService.updateAccount(accountId, updatedData);
            return hasUpdated ? ResponseEntity.status(HttpStatus.OK).body("Account updated successfully.") : ResponseEntity.status(HttpStatus.OK).body("No updates were applied to the account.");
        } catch (Exception e) {
            LOG.error("Error updating account: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating account");
        }
    }

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all accounts", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getAllAccounts() {
        try {
            List<Account> accountDTOS = accountRepositoryHandler.getAllAccounts();
            return ResponseEntity.status(HttpStatus.OK).body(accountDTOS);
        } catch (Exception e) {
            LOG.error("Error retrieving all accounts", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Delete an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the account"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping(value = ACCOUNT_ID, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteAccount(@PathVariable int accountId) {
        try {
            boolean deletedRequested = accountDeletionService.requestAccountDelete(accountId);

            if (deletedRequested) {
                return ResponseEntity.status(HttpStatus.OK).body("Successfully Requested to Delete Account with ID: " + accountId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to request deletion of account with ID: " + accountId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user: " + e.getMessage());
        }
    }
}
