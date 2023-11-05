package com.rahim.userservice.service;

import com.rahim.userservice.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface IUserProfileService {
    void createUserProfile(UserProfile userProfile);
    Optional<UserProfile> findUserProfileById(int id);
    List<UserProfile> findAllUserProfiles();
    void updateUserProfile(UserProfile userProfile);
    Optional<UserProfile> findByUserProfileByUsername(String username);

    void deleteUserProfile(int userId);
}
