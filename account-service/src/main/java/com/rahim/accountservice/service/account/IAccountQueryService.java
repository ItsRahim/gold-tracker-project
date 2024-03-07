package com.rahim.accountservice.service.account;

import com.rahim.accountservice.model.Account;

import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
public interface IAccountQueryService {

    /**
     * This method checks if an account with the given user ID exists and if notifications is enabled
     *
     * @param userId the ID of the user to be checked
     */
    void checkNotificationCriteria(String accountId);

    /**
     * This method retrieves all accounts.
     *
     * @return a list of all accounts
     */
    List<Account> getAllAccounts();

    /**
     * This method finds an account by its ID.
     *
     * @param accountId the ID of the account to be found
     * @return an Optional containing the found account, or an empty Optional if no account was found
     */
    Optional<Account> findAccountById(int accountId);
}
