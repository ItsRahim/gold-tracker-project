package com.rahim.accountservice.repository;

import com.rahim.accountservice.dao.AccountDataAccess;
import com.rahim.accountservice.dao.ProfileDataAccess;
import com.rahim.accountservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    @Query(value = "SELECT * FROM " + ProfileDataAccess.TABLE_NAME +
            " WHERE LOWER(" + ProfileDataAccess.COL_PROFILE_USERNAME + ") = LOWER(:username)", nativeQuery = true)
    Optional<Profile> findByUsername(@Param("username") String username);

    /**
     * This method is used to find a user profile id by a given account id
     *
     * @param id the account id of the user profile to be found
     * @return an int containing the profile id if found, else 0
     */
    @Query(value = "SELECT " + ProfileDataAccess.COL_PROFILE_ID + " FROM " +
            ProfileDataAccess.TABLE_NAME + " WHERE " +
            AccountDataAccess.COL_ACCOUNT_ID + " = :id", nativeQuery = true)
    int getProfileIdByUserId(@Param("id") int id);

    /**
     * This method is used to check if a user profile exists by a given username.
     *
     * @param username the username of the user profile to be checked
     * @return a boolean indicating whether a user profile with the given username exists
     */
    boolean existsByUsernameIgnoreCase(String username);

}