package com.rahim.accountservice.service.repository;

import com.rahim.accountservice.model.Account;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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
     * Checks if an account exists by its email.
     *
     * @param email The email of the account to check if it exists.
     * @return boolean indicating if the account exists
     */
    boolean existsByEmail(String email);

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
    List<Integer> getUsersToDelete(LocalDate cutoffDate);

    /**
     * Retrieves a list of user accounts that are pending deletion.
     *
     * @return A list of {@link Account} objects representing users pending deletion.
     */
    List<Tuple> getPendingDeleteUsers();

    /**
     * Retrieves a list of all user accounts from the database.
     *
     * @return A list of {@link Account} objects representing all accounts. If no accounts are found, an empty list is returned.
     * @throws DataAccessException If an error occurs while fetching accounts.
     */
    List<Account> getAllAccounts();

    /**
     * Retrieves the timestamp when the account with the specified user ID was last updated.
     *
     * @param userId The unique identifier of the user account.
     * @return The `OffsetDateTime` representing the last update timestamp.
     */
    OffsetDateTime getUpdatedAtByUserId(Integer userId);

}
