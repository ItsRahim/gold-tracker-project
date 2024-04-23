package com.rahim.accountservice.service.repository;

import com.rahim.accountservice.model.Profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This interface class is responsible for CRUD operations on the Profile table
 * This utilised by classes and methods within account-service <strong>only</strong>
 *
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
public interface IProfileRepositoryHandler {
    void createNewProfile(Profile profile);
    void updateProfile(Profile profile);
    void deleteProfile(int profileId);
    Optional<Profile> findById(int profileId);
    boolean existsByUsername(String username);
    Map<String, Object> getProfileDetails(int accountId);
    Optional<Profile> getProfileByUsername(String username);
    int getProfileIdByUserId(int userId);
    List<Profile> getAllProfiles();
}
