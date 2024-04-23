package com.rahim.accountservice.controller;

import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.UserRequest;
import com.rahim.accountservice.service.account.IAccountCreationService;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.account.IAccountUpdateService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
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
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);
    private final IAccountCreationService accountCreationService;
    private final IAccountDeletionService accountDeletionService;
    private final IAccountUpdateService accountUpdateService;
    private final IAccountRepositoryHandler accountRepositoryHandler;

    @PostMapping()
    public ResponseEntity<String> createAccount(@RequestBody UserRequest userRequest) {
        try {
            accountCreationService.createAccount(userRequest);
            LOG.info("Successfully Created Account: {}", userRequest.getProfile().getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body("Account and Profile created successfully");
        } catch (Exception e) {
            LOG.error("Error creating Account and Profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Creating Account and Profile");
        }
    }

    @GetMapping(ACCOUNT_ID)
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


    @PutMapping(ACCOUNT_ID)
    public ResponseEntity<String> updateAccount(@PathVariable int accountId,
                                                @RequestBody Map<String, String> updatedData) {
        try {
            OffsetDateTime beforeUpdate = accountRepositoryHandler.getUpdatedAtByUserId(accountId);
            accountUpdateService.updateAccount(accountId, updatedData);
            OffsetDateTime afterUpdate = accountRepositoryHandler.getUpdatedAtByUserId(accountId);

            if (beforeUpdate.equals(afterUpdate)) {
                return ResponseEntity.status(HttpStatus.OK).body("No updates were applied to the account.");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Account updated successfully.");
            }

        } catch (Exception e) {
            LOG.error("Error updating account: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating account");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accountDTOS = accountRepositoryHandler.getAllAccounts();
        return ResponseEntity.status(HttpStatus.OK).body(accountDTOS);
    }

    @DeleteMapping(ACCOUNT_ID)
    public ResponseEntity<String> deleteAccount(@PathVariable int accountId) {
        try {
            boolean deletedRequested = accountDeletionService.requestAccountDelete(accountId);

            if (deletedRequested) {
                return ResponseEntity.status(HttpStatus.OK).body("Successfully Requested to Delete Account with ID: " + accountId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for ID: {} " + accountId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user: " + e.getMessage());
        }
    }
}