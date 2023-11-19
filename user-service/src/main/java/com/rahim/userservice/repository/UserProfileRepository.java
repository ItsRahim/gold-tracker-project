package com.rahim.userservice.repository;

import com.rahim.userservice.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    @Query(value = "SELECT * FROM rgts.user_profiles WHERE LOWER(username) = LOWER(:username)", nativeQuery = true)
    Optional<UserProfile> findByUsername(@Param("username") String username);

    @Query(value = "SELECT profile_id FROM rgts.user_profiles WHERE user_id = :id", nativeQuery = true)
    int getProfileId(@Param("id") int id);

}