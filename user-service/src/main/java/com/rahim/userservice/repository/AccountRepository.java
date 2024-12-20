package com.rahim.userservice.repository;

import com.rahim.userservice.constant.AccountState;
import com.rahim.userservice.dao.AccountDataAccess;
import com.rahim.userservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
            + AccountDataAccess.COL_LAST_LOGIN
            + " < :cutoffDate AND "
            + AccountDataAccess.COL_STATUS
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
            + AccountDataAccess.COL_ID
            + " FROM "
            + AccountDataAccess.TABLE_NAME
            + " WHERE "
            + AccountDataAccess.COL_LAST_LOGIN
            + " < :cutoffDate AND "
            + AccountDataAccess.COL_STATUS
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
            + AccountDataAccess.COL_ID
            + " FROM "
            + AccountDataAccess.TABLE_NAME
            + " WHERE "
            + AccountDataAccess.COL_STATUS
            + " = '"
            + AccountState.PENDING_DELETE
            + "' AND "
            + AccountDataAccess.COL_DELETE_DATE + " = :deleteDate", nativeQuery = true)
    List<Integer> getUsersPendingDeletion(LocalDate deleteDate);

    /**
     * This method is used to check if a user account exists by a given email.
     *
     * @param email the email of the user account to be checked
     * @return a boolean indicating whether a user account with the given email exists
     */
    boolean existsAccountByEmail(String email);

    @Query(value = "SELECT "
            + AccountDataAccess.COL_ID
            + " FROM "
            + AccountDataAccess.TABLE_NAME
            + " WHERE "
            + AccountDataAccess.COL_NOTIFICATION_SETTING
            + " = 'true'", nativeQuery = true)
    List<Integer> getAccountByNotificationSettingTrue();

    @Query(value = "SELECT " + AccountDataAccess.COL_ID + " FROM " + AccountDataAccess.TABLE_NAME, nativeQuery = true)
    List<Integer> getAllAccountId();

}
