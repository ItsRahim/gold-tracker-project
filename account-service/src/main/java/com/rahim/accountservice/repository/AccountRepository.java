package com.rahim.accountservice.repository;

import com.rahim.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * This is a repository interface for the Account table.
 * It extends JpaRepository to provide methods for CRUD (Create, Read, Update, Delete) operations.
 * <p>
 * The repository provides methods to perform operations on Account entities,
 * such as save, find, delete, etc. These operations are performed in the context
 * of managing Account entities in relation to their persistence.
 *
 * @param Account - The entity type the repository manages.
 * @param Integer - The type of the entity's identifier.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    /**
     * This method retrieves a list of user accounts that are deemed to be inactive.
     * An account is considered inactive if the last login date is earlier than the provided cutoff date.
     *
     * @param cutoffDate - The date used to determine inactivity. Any user who has not logged in since this date is considered inactive.
     * @return a list of accounts which are deemed to be inactive based on the cutoff date.
     */
    @Query(value = "SELECT * FROM rgts.user_accounts WHERE last_login < :cutoffDate AND account_status = 'ACTIVE'", nativeQuery = true)
    List<Account> getInactiveUsers(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * This method retrieves a list of user accounts that are eligible for deletion.
     * An account is considered eligible for deletion if it is already 'INACTIVE' and the last login date is earlier than the provided cutoff date plus 7 days.
     *
     * @param cutoffDate - The date used to determine eligibility for deletion. Any 'INACTIVE' user who has not logged in since this date plus 7 days is considered eligible for deletion.
     * @return a list of accounts which are eligible for deletion based on the cutoff date.
     */
    @Query(value = "SELECT * FROM rgts.user_accounts WHERE last_login < :cutoffDate AND account_status = 'INACTIVE'", nativeQuery = true)
    List<Account> getUsersToDelete(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * This method retrieves a list of user accounts that are pending deletion.
     * An account is considered pending deletion if its account status is 'PENDING DELETE'.
     *
     * @return a list of accounts which are pending deletion and ready to be processed.
     */
    @Query(value = "SELECT * FROM rgts.user_accounts WHERE account_status = 'PENDING DELETE'", nativeQuery = true)
    List<Account> getPendingDeleteUsers();

    boolean existsAccountByEmail(String email);
}
