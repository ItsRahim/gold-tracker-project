package com.rahim.userservice.service.repository.implementation;

import com.rahim.userservice.model.Profile;
import com.rahim.userservice.repository.ProfileRepository;
import com.rahim.userservice.service.repository.IProfileRepositoryHandler;
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

@Service
@RequiredArgsConstructor
public class ProfileRepositoryHandlerService implements IProfileRepositoryHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileRepositoryHandlerService.class);
    private final ProfileRepository profileRepository;

    @Override
    public void saveProfile(Profile profile) {
        if(!ObjectUtils.anyNull(profile)) {
            try {
                profileRepository.save(profile);
            } catch (DataException e) {
                LOG.error("Error saving profile to the database", e);
                throw new DataIntegrityViolationException("Error saving profile to database", e);
            }
        } else {
            LOG.error("Profile information provided is null or contains null properties. Unable to save");
            throw new IllegalArgumentException("Profile information provided is null or contains null properties. Unable to save");
        }
    }

    @Override
    public void deleteProfile(int profileId) {
        findById(profileId).ifPresent(profile -> {
            try {
                LOG.info("Deleting account with ID: {}", profileId);
                profileRepository.deleteById(profileId);
                LOG.info("Account with ID {} deleted successfully", profileId);
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
            if(profileOptional.isPresent()) {
                LOG.info("Found user account with ID: {}", profileId);
            } else {
                LOG.info("Account not found for ID: {}", profileId);
            }

            return profileOptional;
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving account with ID: {}", profileId, e);
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return profileRepository.existsByUsername(username);
    }

    @Override
    public Map<String, Object> getProfileDetails(int accountId) {
        Optional<Map<String, Object>> profileDetailsOptional = profileRepository.getProfileDetails(accountId);

        return profileDetailsOptional.orElse(null);
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
