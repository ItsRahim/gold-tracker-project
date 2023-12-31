package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.exception.DuplicateUserException;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.model.Profile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.service.account.IAccountCreationService;
import com.rahim.userservice.service.profile.IProfileCreation;
import com.rahim.userservice.service.profile.IProfileQuery;
import com.rahim.userservice.service.repository.IAccountRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountCreationService implements IAccountCreationService {
    private static final Logger LOG = LoggerFactory.getLogger(AccountCreationService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IProfileCreation profileCreation;
    private final IProfileQuery profileQuery;

    @Override
    @Transactional
    public void createAccount(UserRequest userRequest) throws DuplicateUserException {
        Account account = userRequest.getAccount();
        Profile profile = userRequest.getProfile();

        String email = account.getEmail();
        String username = profile.getUsername();

        boolean accountExists = accountRepositoryHandler.hasAccount(email);
        boolean profileExists = profileQuery.existsByUsername(username);
        boolean anyNullUser = ObjectUtils.anyNull(account);
        boolean anyNullProfile = ObjectUtils.anyNull(profile);

        if (accountExists && profileExists) {
            LOG.warn("Account with email {} and username {} already exists. Not creating duplicate.", email, username);
            throw new DuplicateUserException("Account with email " + email + " and username " + username + " already exists.");
        } else if (accountExists) {
            LOG.warn("Account with email {} already exists. Not creating duplicate.", email);
            throw new DuplicateUserException("Account with email " + email + " already exists.");
        } else if (profileExists) {
            LOG.warn("Account with username {} already exists. Not creating duplicate.", username);
            throw new DuplicateUserException("Account with username " + username + " already exists.");
        }

        if (!anyNullUser || !anyNullProfile) {
            try {
                accountRepositoryHandler.saveAccount(account);
                profileCreation.createProfile(account, profile);

                LOG.info("Successfully created Account and Account Profile for: {}", profile.getUsername());
            } catch (DataIntegrityViolationException e) {
                LOG.error("Error creating Account and Account Profile. Data integrity violation: {}", e.getMessage());
                throw new DuplicateUserException("Account with email " + email + " or username " + username + " already exists.", e);
            } catch (Exception e) {
                LOG.error("Unexpected error creating Account and Account Profile: {}", e.getMessage());
                throw new RuntimeException("Unexpected error creating Account and Account Profile.", e);
            }
        } else {
            LOG.warn("Null values found. Not creating duplicate.");
            throw new DuplicateUserException("Null values found in Account or Profile.");
        }
    }

}
