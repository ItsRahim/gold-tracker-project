package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.exception.DuplicateAccountException;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.model.UserRequest;
import com.rahim.accountservice.service.account.IAccountCreationService;
import com.rahim.accountservice.service.profile.IProfileCreationService;
import com.rahim.accountservice.service.profile.IProfileQueryService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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

        validateInput(account, profile);

        String email = account.getEmail();
        String username = profile.getUsername();

        if (accountRepositoryHandler.hasAccount(email) || profileQuery.existsByUsername(username)) {
            LOG.warn("Account with email {} and/or username {} already exists. Not creating duplicate.", email, username);
            throw new DuplicateAccountException("Account with email " + email + " and username " + username + " already exists.");
        }

        try {
            accountRepositoryHandler.saveAccount(account);
            profileCreation.createProfile(account, profile);

            LOG.info("Successfully created Account and Account Profile for: {}", profile.getUsername());
        } catch (DataAccessException e) {
            LOG.error("Unexpected error creating Account and Account Profile: {}", e.getMessage());
            throw new RuntimeException("Unexpected error creating Account and Account Profile.", e);
        }
    }

    private void validateInput(Account account, Profile profile) {
        if (ObjectUtils.anyNull(account, profile)) {
            LOG.warn("Null values found. Not creating account.");
            throw new IllegalArgumentException("Null values found in Account or Profile.");
        }

        if (StringUtils.isAnyBlank(account.getEmail(), account.getPasswordHash()) ||
            StringUtils.isAnyBlank(profile.getUsername(), profile.getFirstName(),
                                   profile.getLastName(), profile.getContactNumber(), profile.getAddress())) {
            LOG.warn("Null or blank values found in Account or Profile fields. Not creating account.");
            throw new IllegalArgumentException("Null or blank values found in Account or Profile fields.");
        }
    }

}
