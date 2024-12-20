package com.rahim.userservice.service.account;

import com.rahim.userservice.entity.Account;
import com.rahim.userservice.request.account.AccountUpdateRequest;

import java.time.LocalDate;

/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
public interface IAccountUpdateService {

    /**
     * Updates the account with the given ID using the provided updated data.
     *
     * @param accountId   The ID of the account to be updated.
     * @param updatedData The map containing the updated data.
     * @throws RuntimeException If an error occurs while updating the account.
     */
    Object updateAccount(int accountId, AccountUpdateRequest updatedData);

    /**
     * Updates the account object when a deletion request is sent
     *
     * @param account      The account being deleted
     * @param deletionDate The date the account will be deleted
     */
    void updateAccountForDeletion(Account account, LocalDate deletionDate);
}
