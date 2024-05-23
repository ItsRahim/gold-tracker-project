package com.rahim.accountservice.service.account;

import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 23/05/2024
 */
@Service
@RequiredArgsConstructor
public class AccountControllerService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountControllerService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IAccountDeletionService accountDeletionService;

    public ResponseEntity<Object> findAccountById(int accountId) {
        try {
            Account account = accountRepositoryHandler.findById(accountId);
            return (account.getId() != null) ? ResponseEntity.status(HttpStatus.OK).body(account): ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        } catch (Exception e) {
            LOG.error("Error finding user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Error finding account");
        }
    }

    public ResponseEntity<Object> getAllAccounts() {
        try {
            List<Account> accountDTOS = accountRepositoryHandler.getAllAccounts();
            return ResponseEntity.status(HttpStatus.OK).body(accountDTOS);
        } catch (Exception e) {
            LOG.error("An error occurred attempting to get all accounts: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred attempting to get all accounts");
        }
    }

    public ResponseEntity<String> deleteAccount(int accountId) {
        try {
            Account account = accountRepositoryHandler.findById(accountId);
            if (account.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account with ID " + accountId + " does not exist");
            }
            boolean deletedRequested = accountDeletionService.requestAccountDelete(accountId);
            if (deletedRequested) {
                return ResponseEntity.status(HttpStatus.OK).body("Successfully Requested to Delete Account with ID: " + accountId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unable to request deletion of account with ID: " + accountId);
            }
        } catch (Exception e) {
            LOG.error("Error attempting to delete user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Error deleting account");
        }
    }
}
