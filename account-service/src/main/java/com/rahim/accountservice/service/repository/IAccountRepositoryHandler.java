package com.rahim.accountservice.service.repository;

import com.rahim.accountservice.model.Account;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This interface class is responsible for CRUD operations on the Account table
 * This utilised by classes and methods within account-service <strong>only</strong>
 *
 * @author Rahim Ahmed
 * @created 29/12/2023
 */
public interface IAccountRepositoryHandler {

    /**
     * Retrieves an account by its unique ID.
     *
     * @param accountId The ID of the account to retrieve.
     * @return An {@link Optional} containing the account if found, or an empty {@link Optional} if not found.
     * @throws Exception If an error occurs during retrieval.
     */
    Optional<Account> findById(int accountId);

    /**
     * Saves an account to the database.
     *
     * @param account The account to be saved. Must not be null.
     * @throws DataIntegrityViolationException If there is an error saving the account.
     * @throws IllegalArgumentException If the provided account is null or contains null properties.
     */
    void saveAccount(Account account);

    /**
     * Deletes an account by its unique ID.
     *
     * @param accountId The ID of the account to delete.
     * @throws EntityNotFoundException If an error occurs during retrieval.
     */
    void deleteAccount(int accountId);

    /**
     * Deletes an account by its unique ID.
     *
     * @param email The email of the account to check if it exists.
     * @return boolean {@code true} if an account with the given email exists
     */
    boolean hasAccount(String email);

    /**
     * Retrieves a list of inactive user accounts based on the specified cutoff date.
     *
     * @param cutoffDate The date used as the threshold for inactivity.
     * @return A list of {@link Account} objects representing inactive users.
     */
    List<Account> getInactiveUsers(LocalDate cutoffDate);

    /**
     * Retrieves a list of user accounts that are eligible for deletion based on the specified cutoff date.
     *
     * @param cutoffDate The date used as the threshold for identifying inactive accounts.
     * @return A list of {@link Account} objects representing users eligible for deletion.
     */
    List<Account> getUsersToDelete(LocalDate cutoffDate);

    /**
     * Retrieves a list of user accounts that are pending deletion.
     *
     * @return A list of {@link Account} objects representing users pending deletion.
     */
    List<Account> getPendingDeleteUsers();

    /**
     * Retrieves a list of all user accounts from the database.
     *
     * @return A list of {@link Account} objects representing all accounts. If no accounts are found, an empty list is returned.
     * @throws DataAccessException If an error occurs while fetching accounts.
     */
    List<Account> getAllAccounts();

}
