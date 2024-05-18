package com.rahim.accountservice.service.repository.implementation;

import com.rahim.accountservice.dao.AccountDataAccess;
import com.rahim.accountservice.dao.ProfileDataAccess;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.model.EmailToken;
import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.repository.ProfileRepository;
import com.rahim.accountservice.request.AccountJson;
import com.rahim.accountservice.request.ProfileJson;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenRowMapper;
import com.rahim.common.constant.EmailTemplate;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileRepositoryHandlerService implements IProfileRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileRepositoryHandlerService.class);
    private final ProfileRepository profileRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createNewProfile(Profile profile) {
        if (profile == null) {
            LOG.error("Invalid profile. Unable to save.");
            throw new IllegalArgumentException("Invalid profile. Unable to save.");
        }

        try {
            profileRepository.save(profile);
            LOG.debug("New profile created: {}", profile.getId());
        } catch (DataIntegrityViolationException e) {
            LOG.error("Error saving profile to the database: {}", e.getMessage());
            throw new RuntimeException("Error saving profile to database", e);
        }
    }

    @Override
    public void updateProfile(Profile profile) {
        if (profile == null || profile.getId() == null) {
            LOG.error("Invalid profile or profile ID is null. Unable to save.");
            throw new IllegalArgumentException("Invalid profile or profile ID is null. Unable to save.");
        }

        try {
            profileRepository.save(profile);
            LOG.debug("Profile updated: {}", profile.getId());
        } catch (DataIntegrityViolationException e) {
            LOG.error("Error updating profile to the database: {}", e.getMessage());
            throw new RuntimeException("Error saving profile to database", e);
        }
    }


    @Override
    public void deleteProfile(int profileId) {
        findById(profileId).ifPresent(profile -> {
            try {
                LOG.trace("Deleting profile with ID: {}", profileId);
                profileRepository.deleteById(profileId);
                LOG.trace("Profile with ID {} deleted successfully", profileId);
            } catch (Exception e) {
                LOG.warn("Attempted to delete non-existing profile with ID: {}", profileId);
                throw new EntityNotFoundException("Profile with ID " + profileId + " not found");
            }
        });
    }

    @Override
    public Optional<Profile> findById(int profileId) {
        try {
            Optional<Profile> profileOptional = profileRepository.findById(profileId);
            profileOptional.ifPresentOrElse(
                    profile -> LOG.trace("Found profile with ID: {}", profileId),
                    () -> LOG.trace("Profile not found for ID: {}", profileId)
            );
            return profileOptional;
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving profile with ID: {}", profileId, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return profileRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public EmailToken generateEmailTokens(EmailProperty emailProperty) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ");
        sqlBuilder.append("up.").append(ProfileDataAccess.COL_PROFILE_FIRST_NAME).append(" AS ").append(ProfileJson.PROFILE_FIRST_NAME).append(", ");
        sqlBuilder.append("up.").append(ProfileDataAccess.COL_PROFILE_LAST_NAME).append(" AS ").append(ProfileJson.PROFILE_LAST_NAME).append(", ");
        sqlBuilder.append("ua.").append(AccountDataAccess.COL_ACCOUNT_EMAIL).append(" AS ").append(AccountJson.ACCOUNT_EMAIL).append(", ");

        if (emailProperty.isIncludeUsername()) {
            sqlBuilder.append("up.").append(ProfileDataAccess.COL_PROFILE_USERNAME).append(" AS ").append(ProfileJson.PROFILE_USERNAME).append(", ");
        }

        if (emailProperty.isIncludeDate()) {
            String templateName = emailProperty.getTemplateName();
            if (templateName.equals(EmailTemplate.ACCOUNT_DELETION_TEMPLATE)) {
                sqlBuilder.append("ua.").append(AccountDataAccess.COL_ACCOUNT_DELETE_DATE).append(" AS ").append(AccountJson.ACCOUNT_DELETE_DATE).append(", ");
            } else if (templateName.equals(EmailTemplate.ACCOUNT_UPDATE_TEMPLATE)) {
                sqlBuilder.append("ua.").append(AccountDataAccess.COL_ACCOUNT_UPDATED_AT).append(" AS ").append(AccountJson.ACCOUNT_UPDATED_AT).append(", ");
            }
        }

        sqlBuilder.deleteCharAt(sqlBuilder.length() - 2);

        sqlBuilder.append("FROM ").append(ProfileDataAccess.TABLE_NAME).append(" up ");
        sqlBuilder.append("JOIN ").append(AccountDataAccess.TABLE_NAME).append(" ua ");
        sqlBuilder.append("ON up.").append(ProfileDataAccess.COL_ACCOUNT_ID).append(" = ua.").append(AccountDataAccess.COL_ACCOUNT_ID).append(" ");
        sqlBuilder.append("WHERE up.").append(ProfileDataAccess.COL_ACCOUNT_ID).append(" = ").append(emailProperty.getAccountId());

        String sql = sqlBuilder.toString();

        EmailTokenRowMapper emailTokenRowMapper = new EmailTokenRowMapper();
        emailTokenRowMapper.setEmailProperty(emailProperty);

        return jdbcTemplate.queryForObject(sql, emailTokenRowMapper);
    }

    @Override
    public Optional<Profile> getProfileByUsername(String username) {
        return profileRepository.findByUsername(username);
    }

    @Override
    public int getProfileIdByUserId(int userId) {
        return profileRepository.getProfileIdByUserId(userId);
    }

    @Override
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
}
