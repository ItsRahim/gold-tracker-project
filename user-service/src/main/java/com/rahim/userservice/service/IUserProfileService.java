package com.rahim.userservice.service;

import com.rahim.userservice.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface IUserProfileService {
    void createUserProfile(UserProfile userProfile);
    Optional<UserProfile> getProfileById(int id);
    List<UserProfile> getAllProfiles();
    void updateUserProfile(UserProfile userProfile);
    Optional<UserProfile> getProfileByUsername(String username);

    void deleteUserProfile(int userId);
}
