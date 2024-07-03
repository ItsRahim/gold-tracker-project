package com.rahim.userservice.service.repository;

import com.rahim.userservice.entity.Account;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 29/12/2023
 */
public interface IAccountRepositoryHandler {

    /**
     * Finds {@link Account} by given ID
     *
     * @param accountId The account ID to
     * @return The {@link Account} object if found, or null if not found.
     */
    Account findById(int accountId);

    /**
     * Saves the given account
     *
     * @param account The {@link Account} object to save.
     */
    void saveAccount(Account account);

    /**
     * Deletes an account by the given ID.
     *
     * @param accountId The account ID of the account to delete.
     */
    void deleteAccount(int accountId);

    /**
     * Checks if an account with the given email exists.
     *
     * @param email The email to check.
     * @return True if an account with the email exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Gets a list of inactive users as of the given cutoff date.
     *
     * @param cutoffDate The date to check for inactive users.
     * @return A list of inactive {@link Account} objects.
     */
    List<Account> getInactiveUsers(LocalDate cutoffDate);

    /**
     * Gets a list of user IDs to delete as of the given cutoff date.
     *
     * @param cutoffDate The date to check for users to delete.
     * @return A list of user IDs to delete.
     */
    List<Integer> getUsersToDelete(LocalDate cutoffDate);

    /**
     * Gets a list of user IDs pending deletion as of the given deletion date.
     *
     * @param deletionDate The date to check for users pending deletion.
     * @return A list of user IDs pending deletion.
     */
    List<Integer> getUsersPendingDeletion(LocalDate deletionDate);

    /**
     * Gets a list of all accounts.
     *
     * @return A list of all {@link Account} objects.
     */
    List<Account> getAllAccounts();

    /**
     * Gets a list of account IDs for accounts that have active notifications.
     *
     * @return A list of account IDs with active notifications.
     */
    List<Integer> getAccountActiveNotification();

    /**
     * Gets a list of all account IDs.
     *
     * @return A list of all account IDs.
     */
    List<Integer> getAllAccountIds();

}
