package com.rahim.userservice.repository;

import com.rahim.userservice.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    @Query(value = "SELECT * FROM rgts.user_profiles WHERE LOWER(username) = LOWER(:username)", nativeQuery = true)
    Optional<Profile> findByUsername(@Param("username") String username);

    @Query(value = "SELECT profile_id FROM rgts.user_profiles WHERE account_id = :id", nativeQuery = true)
    int getProfileIdByUserId(@Param("id") int id);

    boolean existsByUsername(String username);

        @Query(value = "SELECT up.username, up.first_name, up.last_name, ua.email, ua.delete_date, ua.updated_at " +
                "FROM rgts.user_profiles up JOIN rgts.user_accounts u ON up.account_id = ua.account_id " +
                "WHERE up.account_id = :accountId", nativeQuery = true)
        Optional<Map<String, Object>> getProfileDetails(@Param("accountId") int accountId);
}