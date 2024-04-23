package com.rahim.accountservice.service.account;

/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
public interface IAccountDeletionService {

    /**
     * This method is used to request the deletion of an account.
     *
     * @param accountId the ID of the account to be deleted
     * @return true if the account deletion request is successful, false otherwise
     * @throws RuntimeException if there's an unexpected error during the account update or email token generation
     */
    boolean requestAccountDelete(int accountId);
}
