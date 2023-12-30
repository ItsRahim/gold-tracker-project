package com.rahim.userservice.repository;

import com.rahim.userservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    @Query(value = "SELECT * FROM rgts.user_accounts WHERE last_login < :cutoffDate AND account_status = 'ACTIVE'", nativeQuery = true)
    List<Account> findInactiveUsers(@Param("cutoffDate") LocalDate cutoffDate);

    @Query(value = "SELECT * FROM rgts.user_accounts WHERE last_login < :cutoffDate AND account_status = 'INACTIVE'", nativeQuery = true)
    List<Account> findUsersToDelete(@Param("cutoffDate") LocalDate cutoffDate);

    @Query(value = "SELECT * FROM rgts.user_accounts WHERE account_status = 'PENDING DELETE'", nativeQuery = true)
    List<Account> findPendingDeleteUsers();

    boolean existsByEmail(String email);

    @Query(value = "SELECT :columnName FROM rgts.user_accounts WHERE account_Id = :accountId", nativeQuery = true)
    Optional<LocalDate> findDateByUserId(@Param("accountId") int accountId, @Param("columnName") String columnName);

    @Query(value = "SELECT email FROM rgts.user_accounts WHERE account_id = :accountId", nativeQuery = true)
    Optional<String> findEmailById(@Param("accountId") int accountId);
}
