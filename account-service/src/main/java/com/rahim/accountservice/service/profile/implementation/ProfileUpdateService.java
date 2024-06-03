package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.entity.Profile;
import com.rahim.accountservice.model.Address;
import com.rahim.accountservice.request.profile.ProfileUpdateRequest;
import com.rahim.accountservice.service.profile.IProfileUpdateService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
@Service
@RequiredArgsConstructor
public class ProfileUpdateService implements IProfileUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileUpdateService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Profile updateProfile(int profileId, ProfileUpdateRequest updatedData) {
        Profile profile = profileRepositoryHandler.findById(profileId);

        if (profile == null) {
            LOG.warn("Profile with ID {} not found.", profileId);
            throw new EntityNotFoundException("Profile does not exist. Unable to update");
        }

        try {
            updateProfileData(profile, updatedData);
            profileRepositoryHandler.updateProfile(profile);

            return profile;
        } catch (Exception e) {
            LOG.error("Error updating profile: {}", e.getMessage(), e);
            throw new DatabaseException("An unexpected error occurred whilst updating profile");
        }
    }

    private void updateProfileData(Profile profile, ProfileUpdateRequest updatedData) {
        if (updatedData.getFirstName() != null) {
            profile.setFirstName(updatedData.getFirstName());
        }
        if (updatedData.getLastName() != null) {
            profile.setLastName(updatedData.getLastName());
        }
        if (updatedData.getContactNumber() != null) {
            profile.setContactNumber(updatedData.getContactNumber());
        }
        if (updatedData.getAddress() != null) {
            updateAddress(profile, updatedData.getAddress());
        }
    }

    private void updateAddress(Profile profile, Address address) {
        Address currentAddress = profile.getAddress();
        if (address.getStreet() != null) {
            currentAddress.setStreet(address.getStreet());
        }
        if (address.getCity() != null) {
            currentAddress.setCity(address.getCity());
        }
        if (address.getPostCode() != null) {
            currentAddress.setPostCode(address.getPostCode());
        }
        if (address.getCountry() != null) {
            currentAddress.setCountry(address.getCountry());
        }
    }
}
