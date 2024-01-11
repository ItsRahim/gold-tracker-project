package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.exception.DuplicateAccountException;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.model.Profile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.service.account.IAccountCreationService;
import com.rahim.userservice.service.profile.IProfileCreationService;
import com.rahim.userservice.service.profile.IProfileQueryService;
import com.rahim.userservice.service.repository.IAccountRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountCreationService implements IAccountCreationService {
    private static final Logger LOG = LoggerFactory.getLogger(AccountCreationService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IProfileCreationService profileCreation;
    private final IProfileQueryService profileQuery;

    @Override
    @Transactional
    public void createAccount(UserRequest userRequest) throws DuplicateAccountException {
        Account account = userRequest.getAccount();
        Profile profile = userRequest.getProfile();

        String email = account.getEmail();
        String username = profile.getUsername();

        boolean accountExists = accountRepositoryHandler.hasAccount(email);
        boolean profileExists = profileQuery.existsByUsername(username);
        boolean anyNullUser = ObjectUtils.anyNull(account);
        boolean anyNullProfile = ObjectUtils.anyNull(profile);

        if (accountExists || profileExists) {
            LOG.warn("Account with email {} and/or username {} already exists. Not creating duplicate.", email, username);
            throw new DuplicateAccountException("Account with email " + email + " and username " + username + " already exists.");
        }

        if (!anyNullUser || !anyNullProfile) {
            try {
                accountRepositoryHandler.saveAccount(account);
                profileCreation.createProfile(account, profile);

                LOG.info("Successfully created Account and Account Profile for: {}", profile.getUsername());
            } catch (Exception e) {
                LOG.error("Unexpected error creating Account and Account Profile: {}", e.getMessage());
                throw new RuntimeException("Unexpected error creating Account and Account Profile.", e);
            }
        } else {
            LOG.warn("Null values found. Not creating duplicate.");
            throw new NullPointerException("Null values found in Account or Profile.");
        }
    }

}
