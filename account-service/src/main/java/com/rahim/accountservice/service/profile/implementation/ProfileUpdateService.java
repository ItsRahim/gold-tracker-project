package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.service.profile.IProfileUpdateService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileUpdateService implements IProfileUpdateService {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileQueryService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    public void updateProfile(int profileId, Map<String, String> updatedData) {
        Optional<Profile> profileOptional = profileRepositoryHandler.findById(profileId);

        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();

            try {
                if (updatedData.containsKey("firstName")) {
                    profile.setFirstName(updatedData.get("firstName"));
                }
                if (updatedData.containsKey("lastName")) {
                    profile.setLastName(updatedData.get("lastName"));
                }
                if (updatedData.containsKey("contactNumber")) {
                    profile.setContactNumber(updatedData.get("contactNumber"));
                }
                if (updatedData.containsKey("address")) {
                    profile.setAddress(updatedData.get("address"));
                }

                profileRepositoryHandler.saveProfile(profile);

                LOG.info("Profile with ID {} updated successfully", profileId);
            } catch (Exception e) {
                LOG.error("Error updating profile with ID {}: {}", profileId, e.getMessage());
                throw new RuntimeException("Failed to update user.", e);
            }
        } else {
            LOG.warn("Profile with ID {} not found.", profileId);
            throw new RuntimeException("Account not found.");
        }
    }
}
