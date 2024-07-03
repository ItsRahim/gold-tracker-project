package com.rahim.userservice.service.repository.implementation;

import com.rahim.userservice.dao.AccountDataAccess;
import com.rahim.userservice.dao.ProfileDataAccess;
import com.rahim.userservice.model.EmailProperty;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.model.kafka.AccountEmailData;
import com.rahim.userservice.entity.Profile;
import com.rahim.userservice.repository.ProfileRepository;
import com.rahim.userservice.service.repository.IProfileRepositoryHandler;
import com.rahim.userservice.util.EmailTokenRowMapper;
import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileRepositoryHandlerService implements IProfileRepositoryHandler {

    private static final Logger log = LoggerFactory.getLogger(ProfileRepositoryHandlerService.class);
    private final ProfileRepository profileRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createNewProfile(Profile profile) {
        if (profile == null) {
            log.error("Invalid profile. Unable to save.");
            throw new IllegalArgumentException("Invalid profile. Unable to save.");
        }

        try {
            profileRepository.save(profile);
            log.debug("New profile created: {}", profile.getId());
        } catch (Exception e) {
            log.error("Error saving profile to the database: {}", e.getMessage(), e);
            throw new DatabaseException("Error saving profile to database");
        }
    }

    @Override
    public void updateProfile(Profile profile) {
        if (profile == null || profile.getId() == null) {
            log.error("Invalid profile or profile ID is null. Unable to save.");
            throw new IllegalArgumentException("Invalid profile or profile ID is null. Unable to save.");
        }

        try {
            profileRepository.save(profile);
            log.debug("Profile updated: {}", profile.getId());
        } catch (DataIntegrityViolationException e) {
            log.error("Error updating profile to the database: {}", e.getMessage());
            throw new RuntimeException("Error saving profile to database", e);
        }
    }

    @Override
    public void deleteProfile(int profileId) {
        Profile profile = findById(profileId);

        if (profile.getId() == null) {
            log.warn("Attempted to delete non-existing profile with ID: {}", profileId);
            throw new EntityNotFoundException("Profile with ID " + profileId + " not found");
        }

        profileRepository.deleteById(profileId);
        log.debug("Profile with ID {} deleted successfully", profileId);
    }

    @Override
    public Profile findById(int profileId) {
        return profileRepository.findById(profileId).orElse(new Profile());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return profileRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountEmailData generateEmailTokens(EmailProperty emailProperty) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("up.").append(ProfileDataAccess.COL_PROFILE_FIRST_NAME).append(", ");
        sqlBuilder.append("up.").append(ProfileDataAccess.COL_PROFILE_LAST_NAME).append(", ");
        sqlBuilder.append("ua.").append(AccountDataAccess.COL_EMAIL).append(", ");

        if (emailProperty.isIncludeUsername()) {
            sqlBuilder.append("up.").append(ProfileDataAccess.COL_PROFILE_USERNAME).append(", ");
        }

        if (emailProperty.isIncludeDate()) {
            EmailTemplate templateName = emailProperty.getTemplateName();
            if (templateName.equals(EmailTemplate.ACCOUNT_DELETION)) {
                sqlBuilder.append("ua.").append(AccountDataAccess.COL_DELETE_DATE).append(", ");
            } else if (templateName.equals(EmailTemplate.ACCOUNT_UPDATE)) {
                sqlBuilder.append("ua.").append(AccountDataAccess.COL_UPDATED_AT).append(", ");
            }
        }

        sqlBuilder.deleteCharAt(sqlBuilder.length() - 2);

        sqlBuilder.append("FROM ").append(ProfileDataAccess.TABLE_NAME).append(" up ");
        sqlBuilder.append("JOIN ").append(AccountDataAccess.TABLE_NAME).append(" ua ");
        sqlBuilder.append("ON up.").append(ProfileDataAccess.COL_ACCOUNT_ID).append(" = ua.").append(AccountDataAccess.COL_ID).append(" ");
        sqlBuilder.append("WHERE up.").append(ProfileDataAccess.COL_ACCOUNT_ID).append(" = ").append(emailProperty.getAccountId());

        String sql = sqlBuilder.toString();

        EmailTokenRowMapper emailTokenRowMapper = new EmailTokenRowMapper();
        emailTokenRowMapper.setEmailProperty(emailProperty);

        return jdbcTemplate.queryForObject(sql, emailTokenRowMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public Profile getProfileByUsername(String username) {
        return profileRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found with username: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public int getProfileIdByAccountId(int accountId) {
        return profileRepository.getProfileIdByUserId(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
}
