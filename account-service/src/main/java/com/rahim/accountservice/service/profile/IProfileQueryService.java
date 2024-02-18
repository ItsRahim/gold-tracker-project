package com.rahim.accountservice.service.profile;

import com.rahim.accountservice.model.Profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProfileQueryService {

    /**
     * Checks if a profile exists by username.
     *
     * @param username The username to check.
     * @return true if the profile exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Retrieves the profile details for a given account ID.
     *
     * @param accountId The ID of the account.
     * @return A map containing the profile details, or null if no profile is found.
     */
    Map<String, Object> getProfileDetails(int profileId);

    /**
     * Retrieves a profile by username.
     *
     * @param username The username of the profile.
     * @return An Optional containing the profile if found, or an empty Optional otherwise.
     */
    Optional<Profile> getProfileByUsername(String username);

    /**
     * Retrieves all profiles.
     *
     * @return A list of all profiles.
     */
    List<Profile> getAllProfiles();
}
