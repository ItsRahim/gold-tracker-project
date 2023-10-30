package com.rahim.userservice.service;

import com.rahim.userservice.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface IUserProfileService {
    void save(UserProfile userProfile);
    Optional<UserProfile> findById(int id);
    List<UserProfile> findAllUserProfiles();
    void updateExistingUserProfile(UserProfile userProfile);
}
