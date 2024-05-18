package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.dto.ModelMapper;
import com.rahim.accountservice.exception.DuplicateAccountException;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.model.UserRequest;
import com.rahim.accountservice.service.account.IAccountCreationService;
import com.rahim.accountservice.service.profile.IProfileCreationService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * This service class is responsible for creating new accounts.
 * It implements the IAccountCreationService interface.
 *
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Service
@RequiredArgsConstructor
public class AccountCreationService implements IAccountCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountCreationService.class);

    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IProfileCreationService profileCreation;
    private final IProfileRepositoryHandler profileRepositoryHandler;

    /**
     * @see IAccountCreationService
     */
    @Override
    public void createAccount(UserRequest userRequest) {
        Account account = ModelMapper.INSTANCE.toAccountEntity(userRequest.getAccount());
        Profile profile = ModelMapper.INSTANCE.toProfileEntity(userRequest.getProfile());

        validateInput(account, profile);

        String email = account.getEmail();
        String username = profile.getUsername();

        if (accountRepositoryHandler.existsByEmail(email) || profileRepositoryHandler.existsByUsername(username)) {
            LOG.warn("Account with email and/or username already exists. Not creating duplicate.");
            throw new DuplicateAccountException("Account with email an/or username exists");
        }

        try {
            accountRepositoryHandler.saveAccount(account);
            profileCreation.createProfile(account, profile);

            LOG.debug("Successfully created Account and Account Profile for: {}", profile.getUsername());
        } catch (DataAccessException e) {
            LOG.error("Unexpected error creating Account and Account Profile: {}", e.getMessage());
            throw new RuntimeException("Unexpected error creating Account and Account Profile.", e);
        }
    }

    /**
     * This method is used to validate the input account and profile.
     * It checks if any of the account or profile objects are null, and if any of the fields in these objects are null or blank.
     * If any null or blank values are found, it throws an IllegalArgumentException.
     *
     * @param account the Account object to be validated
     * @param profile the Profile object to be validated
     * @throws IllegalArgumentException if null values are found in the Account or Profile objects, or if null or blank values are found in the fields of these objects
     */
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
