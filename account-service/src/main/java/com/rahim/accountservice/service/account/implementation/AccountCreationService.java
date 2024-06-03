package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.request.ModelMapper;
import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.entity.Profile;
import com.rahim.accountservice.model.UserRequest;
import com.rahim.accountservice.service.account.IAccountCreationService;
import com.rahim.accountservice.service.profile.IProfileCreationService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.DuplicateEntityException;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service class is responsible for creating new accounts.
 * It implements the IAccountCreationService interface.
 *
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCreationService implements IAccountCreationService {

    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IProfileRepositoryHandler profileRepositoryHandler;
    private final IProfileCreationService profileCreation;
    private final CacheManager hazelcastCacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRequest createAccount(UserRequest userRequest) {
        Account account = ModelMapper.INSTANCE.toAccountEntity(userRequest.getAccount());
        Profile profile = ModelMapper.INSTANCE.toProfileEntity(userRequest.getProfile());

        validateInput(account, profile);

        String email = account.getEmail();
        String username = profile.getUsername();

        if (accountRepositoryHandler.existsByEmail(email) || profileRepositoryHandler.existsByUsername(username)) {
            log.warn("Account with email and/or username already exists. Not creating duplicate.");
            throw new DuplicateEntityException("Account with email and/or username already exists. Not creating duplicate.");
        }

        try {
            accountRepositoryHandler.saveAccount(account);
            profileCreation.createProfile(account, profile);
            addToHazelcastSet(account.getId());

            log.debug("Successfully created Account and Profile for: {}", profile.getUsername());
            return userRequest;
        } catch (Exception e) {
            log.error("Unexpected error creating Account and Account Profile: {}", e.getMessage(), e);
            throw new DatabaseException("Unexpected error creating Account and Account Profile.");
        }
    }

    private void addToHazelcastSet(Integer id) {
        hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_SET, id);
        log.debug("Added new account id to hazelcast set");
    }

    private void validateInput(Account account, Profile profile) {
        if (account == null || profile == null) {
            log.warn("Account or profile is null");
            throw new ValidationException("Account or profile is null");
        }

        if (!account.isValid()) {
            log.warn("Email and/or password hash is null for account: {}", account);
            throw new ValidationException("Email and/or password hash is null for account: " + account);
        }

        if (!profile.isValid()) {
            log.warn("Some fields are null or blank for profile: {}", profile);
            throw new ValidationException("Some fields are null or blank for profile: " + profile);
        }
    }

}
