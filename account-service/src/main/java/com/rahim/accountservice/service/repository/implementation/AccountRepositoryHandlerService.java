package com.rahim.accountservice.service.repository.implementation;

import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.repository.AccountRepository;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
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
        return accountRepository.findById(accountId).orElse(new Account());
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
        Account account = findById(accountId);

        if (account.getId() == null) {
            LOG.warn("Attempted to delete non-existing account with ID: {}", accountId);
            throw new EntityNotFoundException("Account with ID " + accountId + " not found");
        }

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
