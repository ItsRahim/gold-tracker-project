package com.rahim.userservice.service.profile.implementation;

import com.rahim.userservice.entity.Profile;
import com.rahim.userservice.model.Address;
import com.rahim.userservice.request.profile.ProfileUpdateRequest;
import com.rahim.userservice.service.profile.IProfileUpdateService;
import com.rahim.userservice.service.repository.IProfileRepositoryHandler;
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

    private static final Logger log = LoggerFactory.getLogger(ProfileUpdateService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Profile updateProfile(int profileId, ProfileUpdateRequest updatedData) {
        Profile profile = profileRepositoryHandler.findById(profileId);

        if (profile == null) {
            log.warn("Profile with ID {} not found.", profileId);
            throw new EntityNotFoundException("Profile does not exist. Unable to update");
        }

        try {
            updateProfileData(profile, updatedData);
            profileRepositoryHandler.updateProfile(profile);

            return profile;
        } catch (Exception e) {
            log.error("Error updating profile: {}", e.getMessage(), e);
            throw new DatabaseException("An unexpected error occurred whilst updating profile");
        }
    }

    /**
     * Updates the profile with the provided updated data.
     *
     * @param profile     The {@link Profile} object to be updated.
     * @param updatedData The {@link ProfileUpdateRequest} object containing the updated profile data.
     */
    private void updateProfileData(Profile profile, ProfileUpdateRequest updatedData) {
        log.info("Entering updateProfileData method with profile: {} and updatedData: {}", profile, updatedData);

        if (updatedData.getFirstName() != null) {
            log.debug("Updating firstName from {} to {}", profile.getFirstName(), updatedData.getFirstName());
            profile.setFirstName(updatedData.getFirstName());
        }
        if (updatedData.getLastName() != null) {
            log.debug("Updating lastName from {} to {}", profile.getLastName(), updatedData.getLastName());
            profile.setLastName(updatedData.getLastName());
        }
        if (updatedData.getContactNumber() != null) {
            log.debug("Updating contactNumber from {} to {}", profile.getContactNumber(), updatedData.getContactNumber());
            profile.setContactNumber(updatedData.getContactNumber());
        }
        if (updatedData.getAddress() != null) {
            log.debug("Updating address from {} to {}", profile.getAddress(), updatedData.getAddress());
            updateAddress(profile, updatedData.getAddress());
        }
    }

    /**
     * Updates the address of the profile with the provided new address data.
     *
     * @param profile The {@link Profile} object whose address needs to be updated.
     * @param address The new {@link Address} object containing the updated address data.
     */
    private void updateAddress(Profile profile, Address address) {
        log.info("Entering updateAddress method with current address: {} and new address: {}", profile.getAddress(), address);

        Address currentAddress = profile.getAddress();
        if (address.getStreet() != null) {
            log.debug("Updating street from {} to {}", currentAddress.getStreet(), address.getStreet());
            currentAddress.setStreet(address.getStreet());
        }
        if (address.getCity() != null) {
            log.debug("Updating city from {} to {}", currentAddress.getCity(), address.getCity());
            currentAddress.setCity(address.getCity());
        }
        if (address.getPostCode() != null) {
            log.debug("Updating postCode from {} to {}", currentAddress.getPostCode(), address.getPostCode());
            currentAddress.setPostCode(address.getPostCode());
        }
        if (address.getCountry() != null) {
            log.debug("Updating country from {} to {}", currentAddress.getCountry(), address.getCountry());
            currentAddress.setCountry(address.getCountry());
        }
    }
}
