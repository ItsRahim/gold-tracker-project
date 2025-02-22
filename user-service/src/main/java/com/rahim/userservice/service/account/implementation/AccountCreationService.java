package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.entity.Account;
import com.rahim.userservice.entity.Profile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.request.account.AccountCreationRequest;
import com.rahim.userservice.request.profile.ProfileCreationRequest;
import com.rahim.userservice.service.account.IAccountCreationService;
import com.rahim.userservice.service.profile.IProfileCreationService;
import com.rahim.userservice.service.repository.IAccountRepositoryHandler;
import com.rahim.userservice.service.repository.IProfileRepositoryHandler;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.DuplicateEntityException;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.userservice.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountCreationService implements IAccountCreationService {

    private static final Logger log = LoggerFactory.getLogger(AccountCreationService.class);

    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IProfileRepositoryHandler profileRepositoryHandler;
    private final IProfileCreationService profileCreation;
    private final CacheManager hazelcastCacheManager;
    private final PasswordUtil passwordUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRequest createAccount(UserRequest userRequest) {
        if (userRequest.getAccount() == null || userRequest.getProfile() == null) {
            log.error("Account and/or profile is null");
            throw new ValidationException("Account and/or profile is null");
        }

        Account account = createAccountFromRequest(userRequest.getAccount());
        Profile profile = createProfileFromRequest(account, userRequest.getProfile());

        validateInput(account, profile);

        if (accountRepositoryHandler.existsByEmail(account.getEmail()) ||
                profileRepositoryHandler.existsByUsername(profile.getUsername())) {
            String message = "Account with email and/or username already exists. Not creating duplicate.";
            log.warn(message);
            throw new DuplicateEntityException(message);
        }

        try {
            log.debug("Creating account with email: {}", account.getEmail());
            accountRepositoryHandler.saveAccount(account);
            profileCreation.createProfile(account, profile);
            addToHazelcastSet(account.getId());
            log.info("Successfully created Account and Profile for: {}", profile.getUsername());
            return userRequest;
        } catch (DataAccessException e) {
            String errorMessage = "Unexpected error creating Account and Account Profile.";
            log.error(errorMessage, e);
            throw new DatabaseException(errorMessage);
        }
    }

    /**
     * Creates an account object with an encrypted password from the provided account creation request.
     *
     * @param accountRequest The request containing account details such as email and password.
     * @return The created {@link Account} object with the encrypted password.
     * @throws ValidationException if the password is empty.
     */
    private Account createAccountFromRequest(AccountCreationRequest accountRequest) {
        String clearPassword = accountRequest.getPassword();
        if (clearPassword == null || clearPassword.isEmpty()) {
            log.error("Password is empty. Cannot create account.");
            throw new ValidationException("Password is empty. Cannot create account.");
        }

        char[] encryptedPassword = passwordUtil.encryptPassword(accountRequest.getPassword());
        return new Account(accountRequest.getEmail(), encryptedPassword);
    }

    /**
     * Creates a profile object from the provided profile creation request and account object.
     *
     * @param account        The {@link Account} object created in the createAccountFromRequest method.
     * @param profileRequest The request containing profile details such as username, first name, last name, contact number, and address.
     * @return The created {@link Profile} object with the provided details.
     */
    private Profile createProfileFromRequest(Account account, ProfileCreationRequest profileRequest) {
        return new Profile(account, profileRequest.getUsername(), profileRequest.getFirstName(),
                profileRequest.getLastName(), profileRequest.getContactNumber(), profileRequest.getAddress());
    }

    private void addToHazelcastSet(Integer id) {
        hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_SET, id);
        log.debug("Added new account id to hazelcast set");
    }

    /**
     * Validates if the {@link Account} and {@link Profile} objects created from the user request are valid.
     *
     * @param account The {@link Account} object to be validated.
     * @param profile The {@link Profile} object to be validated.
     * @throws ValidationException if either the account or profile is null, or if any required fields in these objects are invalid.
     */
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
