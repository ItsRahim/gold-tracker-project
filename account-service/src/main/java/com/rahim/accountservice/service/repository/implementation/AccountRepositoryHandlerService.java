package com.rahim.accountservice.service.repository.implementation;

import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.repository.AccountRepository;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.EntityNotFoundException;
import com.rahim.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

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
    public Account findById(int accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
    }

    @Override
    public void saveAccount(Account account) {
        if (account == null) {
            LOG.error("Account information provided is null or contains null properties. Unable to save");
            throw new ValidationException("Account information provided is null or contains null properties. Unable to save");
        }

        try {
            accountRepository.save(account);
        } catch (DataAccessException e) {
            LOG.error("Error saving account to the database:", e);
            throw new DatabaseException("Error saving account to the database");
        }
    }

    @Override
    public void deleteAccount(int accountId) {
        findById(accountId);

        accountRepository.deleteById(accountId);
        LOG.debug("Account with ID {} deleted successfully", accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return accountRepository.existsAccountByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getInactiveUsers(LocalDate cutoffDate) {
        return accountRepository.getInactiveUsers(cutoffDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getUsersToDelete(LocalDate cutoffDate) {
        return accountRepository.getUsersToDelete(cutoffDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getUsersPendingDeletion(LocalDate deletionDate) {
        return accountRepository.getUsersPendingDeletion(deletionDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public OffsetDateTime getUpdatedAtByUserId(Integer userId) {
        return accountRepository.findUpdatedAtByUserId(userId).atOffset(ZoneOffset.UTC);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getAccountActiveNotification() {
        return accountRepository.getAccountByNotificationSettingTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getAllAccountIds() {
        return accountRepository.getAllAccountId();
    }

}
