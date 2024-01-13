package com.rahim.accountservice.service.repository;

import com.rahim.accountservice.model.Profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProfileRepositoryHandler {
    void saveProfile(Profile profile);
    void deleteProfile(int profileId);
    Optional<Profile> findById(int profileId);
    boolean existsByUsername(String username);
    Map<String, Object> getProfileDetails(int accountId);
    Optional<Profile> getProfileByUsername(String username);
    int getProfileIdByUserId(int userId);
    List<Profile> getAllProfiles();
}
