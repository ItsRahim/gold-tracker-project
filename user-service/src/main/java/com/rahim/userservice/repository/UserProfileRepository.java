package com.rahim.userservice.repository;

import com.rahim.userservice.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    @Query(value = "SELECT * FROM rgts.user_profiles WHERE LOWER(username) = LOWER(:username)", nativeQuery = true)
    Optional<UserProfile> findByUsername(@Param("username") String username);

    @Query(value = "SELECT profile_id FROM rgts.user_profiles WHERE user_id = :id", nativeQuery = true)
    int getProfileId(@Param("id") int id);

    @Query(value = "SELECT first_name as firstName, last_name as lastName, username FROM rgts.user_profiles WHERE user_id = :id", nativeQuery = true)
    Optional<Map<String, Object>> getEmailTokens(@Param("id") int id);

    boolean existsByUsername(String username);

    @Query(value = "SELECT up.username, up.first_name as firstName, up.last_name as lastName, u.email, u.delete_date as deleteDate, u.updated_at as updatedAt " +
            "FROM rgts.user_profiles up JOIN rgts.users u ON up.user_id = u.user_id " +
            "WHERE up.user_id = :userId", nativeQuery = true)
    Optional<Map<String, Object>> getUserProfileDetails(@Param("userId") int userId);
}