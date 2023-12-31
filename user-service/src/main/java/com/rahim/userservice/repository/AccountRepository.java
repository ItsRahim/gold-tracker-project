package com.rahim.userservice.repository;

import com.rahim.userservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    @Query(value = "SELECT * FROM rgts.user_accounts WHERE last_login < :cutoffDate AND account_status = 'ACTIVE'", nativeQuery = true)
    List<Account> getInactiveUsers(@Param("cutoffDate") LocalDate cutoffDate);

    @Query(value = "SELECT * FROM rgts.user_accounts WHERE last_login < :cutoffDate AND account_status = 'INACTIVE'", nativeQuery = true)
    List<Account> getUsersToDelete(@Param("cutoffDate") LocalDate cutoffDate);

    @Query(value = "SELECT * FROM rgts.user_accounts WHERE account_status = 'PENDING DELETE'", nativeQuery = true)
    List<Account> getPendingDeleteUsers();

    boolean existsAccountByEmail(String email);
}
