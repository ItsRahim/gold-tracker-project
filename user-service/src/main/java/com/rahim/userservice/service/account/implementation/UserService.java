package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.constant.TopicConstants;
import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.exception.DuplicateUserException;
import com.rahim.userservice.exception.UserNotFoundException;
import com.rahim.userservice.kafka.IKafkaService;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.model.Profile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.repository.AccountRepository;
import com.rahim.userservice.service.account.IUserService;
import com.rahim.userservice.service.profile.IUserProfileService;
import com.rahim.userservice.util.IEmailTokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final AccountRepository accountRepository;
    private final IUserProfileService userProfileService;
    private final IEmailTokenGenerator emailTokenGenerator;
    private final IKafkaService kafkaService;
    private final TopicConstants topicConstants;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Override
    @Transactional
    public void createUserAndProfile(UserRequest userRequest) throws DuplicateUserException {
        Account account = userRequest.getAccount();
        Profile profile = userRequest.getProfile();

        String email = account.getEmail();
        String username = profile.getUsername();

        boolean userExists = userExist(email, username);
        boolean anyNullUser = ObjectUtils.anyNull(account);
        boolean anyNullProfile = ObjectUtils.anyNull(profile);

        if (!userExists && (!anyNullUser || !anyNullProfile)) {
            try {
                accountRepository.save(account);

                profile.setAccount(account);
                userProfileService.createUserProfile(profile);

                LOG.info("Successfully created Account and Account Profile for: {}", profile.getUsername());
            } catch (DataIntegrityViolationException e) {
                LOG.error("Error creating Account and Account Profile. Data integrity violation: {}", e.getMessage());
                throw new DuplicateUserException("Account with email " + email + " or username " + username + " already exists.", e);
            } catch (Exception e) {
                LOG.error("Unexpected error creating Account and Account Profile: {}", e.getMessage());
                throw new RuntimeException("Unexpected error creating Account and Account Profile.", e);
            }
        } else {
            LOG.warn("Account with email {} or username {} already exists or null values found. Not creating duplicate.", email, username);
            throw new DuplicateUserException("Account with email " + email + " or username " + username + " already exists or null values found.");
        }
    }

    private boolean userExist(String email, String username) {
        return accountRepository.existsByEmail(email) || userProfileService.checkUsernameExists(username);
    }

    @Override
    public Optional<Account> findUserById(int userId) {
        try {
            return accountRepository.findById(userId);
        } catch (Exception e) {
            LOG.error("Error while finding a user with ID: {}", userId, e);
            throw new UserNotFoundException("Error finding a user by ID");
        }
    }

    @Override
    public List<Account> findAllUsers() {
        List<Account> accounts = accountRepository.findAll();

        if (!accounts.isEmpty()) {
            LOG.info("Found {} accounts in the database", accounts.size());
        } else {
            LOG.info("No accounts found in the database");
        }

        return accounts;
    }

    @Override
    public boolean deleteUserRequest(int userId) {
        Optional<Account> existingUserOptional = accountRepository.findById(userId);

        if (existingUserOptional.isPresent()) {
            Account account = existingUserOptional.get();

            String accountStatus = account.getAccountStatus();

            if (accountStatus.equals(AccountState.ACTIVE.getStatus())) {
                LocalDate deletionDate = LocalDate.now().plusDays(30);

                account.setAccountStatus(AccountState.PENDING_DELETE.getStatus());
                account.setAccountLocked(true);
                account.setNotificationSetting(false);
                account.setDeleteDate(deletionDate);

                accountRepository.save(account);
                emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_DELETION.getTemplateName(), userId, true, true);
                LOG.info("Account with ID {} is pending deletion on {}", userId, deletionDate);

                return true;
            } else {
                LOG.info("Account with ID {} is not eligible for deletion", userId);
            }
        } else {
            LOG.warn("Account with ID {} not found.", userId);
        }

        return false;
    }

    @Override
    public void updateUser(int userId, Map<String, String> updatedData) {
        Optional<Account> existingUserOptional = findUserById(userId);

        if (existingUserOptional.isPresent()) {
            Account account = existingUserOptional.get();

            String oldEmail = account.getEmail();

            try {
                if (updatedData.containsKey("email")) {
                    account.setEmail(updatedData.get("email"));
                }
                if (updatedData.containsKey("passwordHash")) {
                    account.setPasswordHash(updatedData.get("passwordHash"));
                }

                accountRepository.save(account);
                emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName(), userId, true, true, oldEmail);
                LOG.info("Account with ID {} updated successfully", userId);
            } catch (Exception e) {
                LOG.error("Error updating account with ID {}: {}", userId, e.getMessage());
                throw new RuntimeException("Failed to update account.", e);
            }
        } else {
            LOG.warn("Account with ID {} not found.", userId);
            throw new UserNotFoundException("Account with ID " + userId + " not found");
        }
    }

    @Override
    public void deleteUserAccount(int userId) {
        try {
            accountRepository.deleteById(userId);
            LOG.info("Account account with ID {} deleted successfully.", userId);
        } catch (Exception e) {
            LOG.error("Error deleting user account with ID {}: {}", userId, e.getMessage(), e);
        }
    }

    @Override
    public void existsById(String userId) {
        int id = Integer.parseInt(userId);
        String found = String.valueOf(findUserById(id).isPresent());

        kafkaService.sendMessage(topicConstants.getSendIdResult(), found);
    }

}