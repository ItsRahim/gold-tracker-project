package com.rahim.userservice.repository;

import com.rahim.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "SELECT * FROM rgts.users WHERE last_login < :cutoffDate AND account_status = 'ACTIVE'", nativeQuery = true)
    List<User> findInactiveUsers(@Param("cutoffDate") LocalDate cutoffDate);

    @Query(value = "SELECT * FROM rgts.users WHERE last_login < :cutoffDate AND account_status = 'INACTIVE'", nativeQuery = true)
    List<User> findUsersToDelete(@Param("cutoffDate") LocalDate cutoffDate);

    @Query(value = "SELECT * FROM rgts.users WHERE account_status = 'PENDING DELETE'", nativeQuery = true)
    List<User> findPendingDeleteUsers();

    boolean existsByEmail(String email);

    @Query(value = "SELECT :columnName FROM rgts.users WHERE user_id = :userId", nativeQuery = true)
    Optional<LocalDate> findDateByUserId(@Param("userId") int userId, @Param("columnName") String columnName);
    @Query(value = "SELECT email FROM rgts.users WHERE user_id = :userId", nativeQuery = true)
    Optional<String> findEmailById(@Param("userId") int userId);
}
