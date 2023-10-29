package com.rahim.userservice.service;

import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService implements  IUserProfileService {
    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;

    @Override
    public void save(UserProfile userProfile) {
        logger.info("Saving user: {}", userProfile);
        userProfileRepository.save(userProfile);
    }

    @Override
    public Optional<UserProfile> findById(int id) {
        logger.debug("Finding user by ID: {}", id);
        return userProfileRepository.findById(id);
    }

    @Override
    public List<UserProfile> findAllUserProfiles() {
        logger.info("Finding all users");
        return userProfileRepository.findAll();
    }

    @Override
    public void update(UserProfile userProfile) {
        Optional<UserProfile> existingUserProfileOptional = userProfileRepository.findById(userProfile.getId());

        if (existingUserProfileOptional.isPresent()) {
            UserProfile existingUserProfile = existingUserProfileOptional.get();

            existingUserProfile.setUsername(userProfile.getUsername());
            existingUserProfile.setFirstName(userProfile.getFirstName());
            existingUserProfile.setLastName(userProfile.getLastName());
            existingUserProfile.setContactNumber(userProfile.getContactNumber());
            existingUserProfile.setAddress(userProfile.getAddress());

            userProfileRepository.save(existingUserProfile);
        } else {
            logger.error("User Profile not found with ID: {}", userProfile.getId());
            throw new EntityNotFoundException();
        }
    }

}
