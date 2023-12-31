package com.rahim.userservice.service.repository.implementation;

import com.rahim.userservice.model.Account;
import com.rahim.userservice.repository.AccountRepository;
import com.rahim.userservice.service.repository.IAccountRepositoryHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountRepositoryHandlerService implements IAccountRepositoryHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AccountRepositoryHandlerService.class);
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findById(int accountId) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(accountId);
            if(accountOptional.isPresent()) {
                LOG.info("Found user account with ID: {}", accountId);
            } else {
                LOG.info("Account not found for ID: {}", accountId);
            }

            return accountOptional;
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving account with ID: {}", accountId, e);
            return Optional.empty();
        }
    }

    @Override
    public void saveAccount(Account account) {
        if(!ObjectUtils.anyNull(account)) {
            try {
                accountRepository.save(account);
            } catch (DataException e) {
                LOG.error("Error saving account to the database", e);
                throw new DataIntegrityViolationException("Error saving account to database", e);
            }
        } else {
            LOG.error("Account information provided is null or contains null properties. Unable to save");
            throw new IllegalArgumentException("Account information provided is null or contains null properties. Unable to save");
        }
    }

    @Override
    public void deleteAccount(int accountId) {
        findById(accountId).ifPresent(userAccount -> {
            try {
                LOG.info("Deleting account with ID: {}", accountId);
                accountRepository.deleteById(accountId);
                LOG.info("Account with ID {} deleted successfully", accountId);
            } catch (Exception e) {
                LOG.warn("Attempted to delete non-existing account with ID: {}", accountId);
                throw new EntityNotFoundException("Account with ID " + accountId + " not found");
            }
        });
    }

    @Override
    public boolean hasAccount(String email) {
        LOG.info("Checking if an account exists for email: {}", email);
        return accountRepository.existsAccountByEmail(email);
    }

    @Override
    public List<Account> getInactiveUsers(LocalDate cutoffDate) {
        return accountRepository.getInactiveUsers(cutoffDate);
    }

    @Override
    public List<Account> getUsersToDelete(LocalDate cutoffDate) {
        return accountRepository.getUsersToDelete(cutoffDate);
    }

    @Override
    public List<Account> getPendingDeleteUsers() {
        return accountRepository.getPendingDeleteUsers();
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
