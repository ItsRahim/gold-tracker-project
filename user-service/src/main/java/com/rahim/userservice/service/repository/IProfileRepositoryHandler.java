package com.rahim.userservice.service.repository;

import com.rahim.userservice.model.EmailProperty;
import com.rahim.common.model.kafka.AccountEmailData;
import com.rahim.userservice.entity.Profile;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
public interface IProfileRepositoryHandler {

    /**
     * Creates a new profile.
     *
     * @param profile The {@link Profile} object to create.
     */
    void createNewProfile(Profile profile);

    /**
     * Updates the given profile.
     *
     * @param profile The {@link Profile} object to update.
     */
    void updateProfile(Profile profile);

    /**
     * Deletes a profile by the given ID.
     *
     * @param profileId The profile ID of the profile to delete.
     */
    void deleteProfile(int profileId);

    /**
     * Finds a {@link Profile} by the given ID.
     *
     * @param profileId The profile ID to search for.
     * @return The {@link Profile} object if found, or null if not found.
     */
    Profile findById(int profileId);

    /**
     * Checks if a profile with the given username exists.
     *
     * @param username The username to check.
     * @return True if a profile with the username exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Generates email tokens for the given email property.
     *
     * @param emailProperty The {@link EmailProperty} object containing email properties.
     * @return The {@link AccountEmailData} object containing the email tokens.
     */
    AccountEmailData generateEmailTokens(EmailProperty emailProperty);

    /**
     * Gets a profile by the given username.
     *
     * @param username The username to search for.
     * @return The {@link Profile} object if found, or null if not found.
     */
    Profile getProfileByUsername(String username);

    /**
     * Gets the profile ID by the given account ID.
     *
     * @param accountId The account ID to search for.
     * @return The profile ID if found, or -1 if not found.
     */
    int getProfileIdByAccountId(int accountId);

    /**
     * Gets a list of all profiles.
     *
     * @return A list of all {@link Profile} objects.
     */
    List<Profile> getAllProfiles();
}
