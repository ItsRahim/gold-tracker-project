package com.rahim.accountservice.repository;

import com.rahim.accountservice.constant.AccountState;
import com.rahim.accountservice.dao.AccountDataAccess;
import com.rahim.accountservice.model.Account;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 29/10/2023
 */
@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {

    /**
     * This method retrieves a list of user accounts that are deemed to be inactive (if the last login date is earlier than the provided cutoff date)
     *
     * @param cutoffDate - The date used to determine inactivity. Any user who has not logged in since this date is considered inactive.
     * @return a list of accounts which are deemed to be inactive based on the cutoff date.
     */
    @Query(value = "SELECT * FROM "
            + AccountDataAccess.TABLE_NAME
            + " WHERE "
            + AccountDataAccess.COL_ACCOUNT_LAST_LOGIN
            + " < :cutoffDate AND "
            + AccountDataAccess.COL_ACCOUNT_STATUS
            + " = '"
            + AccountState.ACTIVE
            + "'", nativeQuery = true)
    List<Account> getInactiveUsers(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * This method retrieves a list of user accounts that are eligible for deletion.
     * An account is considered eligible for deletion if it is already 'INACTIVE' and the last login date is earlier than the provided cutoff date plus 7 days.
     *
     * @param cutoffDate - The date used to determine eligibility for deletion. Any 'INACTIVE' user who has not logged in since this date plus 7 days is considered eligible for deletion.
     * @return a list of account ids which are eligible for deletion
     */
    @Query(value = "SELECT "
            + AccountDataAccess.COL_ACCOUNT_ID
            + " FROM "
            + AccountDataAccess.TABLE_NAME
            + " WHERE "
            + AccountDataAccess.COL_ACCOUNT_LAST_LOGIN
            + " < :cutoffDate AND "
            + AccountDataAccess.COL_ACCOUNT_STATUS
            + " = '"
            + AccountState.INACTIVE
            + "'", nativeQuery = true)
    List<Integer> getUsersToDelete(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * This method retrieves a list of account ids which are pending deletion
     *
     * @return a list of accounts which are pending deletion and ready to be processed.
     */
    @Query(value = "SELECT "
            + AccountDataAccess.COL_ACCOUNT_ID
            + ","
            + AccountDataAccess.COL_ACCOUNT_DELETE_DATE
            + " FROM "
            + AccountDataAccess.TABLE_NAME
            + " WHERE "
            + AccountDataAccess.COL_ACCOUNT_STATUS
            + " = '"
            + AccountState.PENDING_DELETE
            + "'", nativeQuery = true)
    List<Tuple> getPendingDeleteUsers();

    /**
     * This method is used to check if a user account exists by a given email.
     *
     * @param email the email of the user account to be checked
     * @return a boolean indicating whether a user account with the given email exists
     */
    boolean existsAccountByEmail(String email);

    /**
     * Retrieves the timestamp when the account with the specified user ID was last updated.
     *
     * @param userId The unique identifier of the user account.
     * @return Instant representing the last update timestamp.
     */
    @Query(value = "SELECT "
            + AccountDataAccess.COL_ACCOUNT_UPDATED_AT
            + " FROM "
            + AccountDataAccess.TABLE_NAME
            + " WHERE "
            + AccountDataAccess.COL_ACCOUNT_ID
            + " = :userId", nativeQuery = true)
    Instant findUpdatedAtByUserId(@Param("userId") Integer userId);

    @Query(value = "SELECT "
            + AccountDataAccess.COL_ACCOUNT_ID
            + " FROM "
            + AccountDataAccess.TABLE_NAME
            + " WHERE "
            + AccountDataAccess.COL_ACCOUNT_NOTIFICATION_SETTING
            + " = 'true'", nativeQuery = true)
    List<Integer> getAccountByNotificationSettingTrue();

    @Query(value = "SELECT " + AccountDataAccess.COL_ACCOUNT_ID + " FROM " + AccountDataAccess.TABLE_NAME, nativeQuery = true)
    List<Integer> getAllAccountId();

}
