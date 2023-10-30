package com.rahim.userservice.service;

import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService implements  IUserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public void save(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    public Optional<UserProfile> findById(int id) {
        return userProfileRepository.findById(id);
    }

    @Override
    public List<UserProfile> findAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    @Override
    public void updateExistingUserProfile(UserProfile userProfile) {
        userProfileRepository.findById(userProfile.getId())
                .ifPresent(existingUserProfile -> {
                    existingUserProfile.setUsername(userProfile.getUsername());
                    existingUserProfile.setFirstName(userProfile.getFirstName());
                    existingUserProfile.setLastName(userProfile.getLastName());
                    existingUserProfile.setContactNumber(userProfile.getContactNumber());
                    existingUserProfile.setAddress(userProfile.getAddress());
                    userProfileRepository.save(existingUserProfile);
                });
    }
}
