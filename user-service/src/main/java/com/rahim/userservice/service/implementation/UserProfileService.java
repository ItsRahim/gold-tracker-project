package com.rahim.userservice.service.implementation;

import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.repository.UserProfileRepository;
import com.rahim.userservice.service.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService implements IUserProfileService {

    private final UserProfileRepository userProfileRepository;
    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);

    @Override
    public void createUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    public Optional<UserProfile> findUserProfileById(int id) {
        return userProfileRepository.findById(id);
    }

    @Override
    public List<UserProfile> findAllUserProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();

        if(!userProfiles.isEmpty()) {
            log.info("Found {} users in the database", userProfiles.size());
        } else {
            log.info("No users found in the database");
        }

        return userProfiles;
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        log.info("Updating profile with ID: {}", userProfile.getId());

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

    @Override
    public Optional<UserProfile> findByUserProfileByUsername(String username) {
        return userProfileRepository.findByUsername(username);
    }

    @Override
    public void deleteUserProfile(int userId) {
        int profileId = userProfileRepository.getProfileId(userId);
        userProfileRepository.deleteById(profileId);
    }
}
