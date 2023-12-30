package com.rahim.userservice.service.profile;

import com.rahim.userservice.model.Profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserProfileService {
    void createUserProfile(Profile profile);
    Optional<Profile> getProfileById(int id);
    List<Profile> getAllProfiles();
    void updateUserProfile(int userId, Map<String, String> updatedData);
    Optional<Profile> getProfileByUsername(String username);
    void deleteUserProfile(int userId);
    boolean checkUsernameExists(String username);
    Map<String, Object> getUserProfileDetails(int userId);
}
