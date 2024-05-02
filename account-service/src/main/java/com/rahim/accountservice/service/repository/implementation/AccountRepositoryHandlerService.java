package com.rahim.accountservice.service.repository.implementation;

import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.repository.AccountRepository;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 29/12/2023
 */
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
                LOG.debug("Found user account with ID: {}", accountId);
            } else {
                LOG.debug("Account not found for ID: {}", accountId);
            }

            return accountOptional;
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving account with ID: {}", accountId, e);
            return Optional.empty();
        }
    }

    @Override
    public void saveAccount(Account account) {
        if(account != null) {
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
                LOG.debug("Deleting account with ID: {}", accountId);
                accountRepository.deleteById(accountId);
                LOG.debug("Account with ID {} deleted successfully", accountId);
            } catch (EmptyResultDataAccessException e) {
                LOG.warn("Attempted to delete non-existing account with ID: {}", accountId);
                throw new EntityNotFoundException("Account with ID " + accountId + " not found");
            }
        });
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.existsAccountByEmail(email);
    }

    @Override
    public List<Account> getInactiveUsers(LocalDate cutoffDate) {
        return accountRepository.getInactiveUsers(cutoffDate);
    }

    @Override
    public List<Integer> getUsersToDelete(LocalDate cutoffDate) {
        return accountRepository.getUsersToDelete(cutoffDate);
    }

    @Override
    public List<Tuple> getPendingDeleteUsers() {
        return accountRepository.getPendingDeleteUsers();
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = Collections.emptyList();

        try {
            accounts = accountRepository.findAll();
            if (CollectionUtils.isEmpty(accounts)) {
                LOG.debug("No accounts found in the database");
            } else {
                LOG.debug("Fetched {} accounts from the database", accounts.size());
            }

        } catch (DataAccessException e) {
            LOG.error("Error occurred while fetching accounts from the database", e);
        }

        return accounts;
    }

    @Override
    public OffsetDateTime getUpdatedAtByUserId(Integer userId) {
        return accountRepository.findUpdatedAtByUserId(userId).atOffset(ZoneOffset.UTC);
    }

    @Override
    public List<Integer> getAccountActiveNotification() {
        return accountRepository.getAccountByNotificationSettingTrue();
    }

}
