package com.rahim.accountservice.repository;

import com.rahim.accountservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 29/10/2023
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    /**
     * This method is used to find a user profile by username in a case-insensitive manner.
     *
     * @param username the username of the user profile to be found
     * @return an Optional containing the Profile object if found, else an empty Optional
     */
    @Query(value = "SELECT * FROM rgts.user_profiles WHERE LOWER(username) = LOWER(:username)", nativeQuery = true)
    Optional<Profile> findByUsername(@Param("username") String username);

    /**
     * This method is used to find a user profile id by a given account id
     *
     * @param id the account id of the user profile to be found
     * @return an int containing the profile id if found, else 0
     */
    @Query(value = "SELECT profile_id FROM rgts.user_profiles WHERE account_id = :id", nativeQuery = true)
    int getProfileIdByUserId(@Param("id") int id);

    /**
     * This method is used to check if a user profile exists by a given username.
     *
     * @param username the username of the user profile to be checked
     * @return a boolean indicating whether a user profile with the given username exists
     */
    boolean existsByUsername(String username);

    /**
     * This method is used to fetch detailed profile information for a given account id.
     * The information includes username, first name, last name, email, deletion date, and last updated date.
     * The fetched data is cleaned up by the account-service and sent to the email-service to populate an email template.
     *
     * @param accountId the account id of the user profile to be found
     * @return an Optional containing a Map with the profile details if found, else an empty Optional
     */
    @Query(value = "SELECT up.username, up.first_name, up.last_name, ua.email, ua.delete_date, ua.updated_at " +
            "FROM rgts.user_profiles up JOIN rgts.user_accounts ua ON up.account_id = ua.account_id " +
            "WHERE up.account_id = :accountId", nativeQuery = true)
    Optional<Map<String, Object>> getProfileDetails(@Param("accountId") int accountId);
}