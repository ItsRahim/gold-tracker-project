package com.rahim.accountservice.service.account;

import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
public interface IAccountUpdateService {

    /**
     * Updates the account with the given ID using the provided updated data.
     *
     * @param accountId The ID of the account to be updated.
     * @param updatedData The map containing the updated data.
     * @throws RuntimeException If an error occurs while updating the account.
     */
    void updateAccount(int userId, Map<String, String> updatedData);
}
