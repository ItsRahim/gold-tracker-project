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

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountRepositoryHandlerService implements IAccountRepositoryHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AccountRepositoryHandlerService.class);
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findById(int userId) {
        try {
            Optional<Account> userOptional = accountRepository.findById(userId);
            if(userOptional.isPresent()) {
                LOG.info("Found user account with ID: {}", userId);
            } else {
                LOG.info("Account not found for ID: {}", userId);
            }

            return userOptional;
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving user with ID: {}", userId, e);
            return Optional.empty();
        }
    }

    @Override
    public void saveUserAccount(Account account) {
        if(!ObjectUtils.anyNull(account)) {
            try {
                accountRepository.save(account);
            } catch (DataException e) {
                LOG.error("Error saving account account to the database", e);
                throw new DataIntegrityViolationException("Error saving account account to database", e);
            }
        } else {
            LOG.error("Account information provided is null or contains null properties. Unable to save");
            throw new IllegalArgumentException("Account information provided is null or contains null properties. Unable to save");
        }
    }

    @Override
    public void deleteUserAccount(int userId) {
        findById(userId).ifPresent(userAccount -> {
            try {
                LOG.info("Deleting user account with ID: {}", userId);
                accountRepository.deleteById(userId);
                LOG.info("Account account with ID {} deleted successfully", userId);
            } catch (Exception e) {
                LOG.warn("Attempted to delete non-existing user account with ID: {}", userId);
                throw new EntityNotFoundException("Account account with ID " + userId + " not found");
            }
        });
    }
}
