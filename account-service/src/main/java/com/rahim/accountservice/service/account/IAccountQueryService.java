package com.rahim.accountservice.service.account;

import com.rahim.accountservice.model.Account;

import java.util.List;

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

}
