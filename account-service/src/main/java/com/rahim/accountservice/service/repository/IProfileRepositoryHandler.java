package com.rahim.accountservice.service.repository;

import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.model.EmailToken;
import com.rahim.accountservice.model.Profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
public interface IProfileRepositoryHandler {
    void createNewProfile(Profile profile);
    void updateProfile(Profile profile);
    void deleteProfile(int profileId);
    Optional<Profile> findById(int profileId);
    boolean existsByUsername(String username);
    EmailToken generateEmailTokens(EmailProperty emailProperty);
    Optional<Profile> getProfileByUsername(String username);
    int getProfileIdByUserId(int userId);
    List<Profile> getAllProfiles();
}
