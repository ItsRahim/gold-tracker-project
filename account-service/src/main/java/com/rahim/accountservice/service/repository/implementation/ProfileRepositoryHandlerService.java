package com.rahim.accountservice.service.repository.implementation;

import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.repository.ProfileRepository;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Service
@RequiredArgsConstructor
public class ProfileRepositoryHandlerService implements IProfileRepositoryHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileRepositoryHandlerService.class);
    private final ProfileRepository profileRepository;

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
    public Map<String, Object> getProfileDetails(int accountId) {
        return profileRepository.getProfileDetails(accountId).orElse(null);
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
