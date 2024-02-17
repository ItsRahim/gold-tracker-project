package com.rahim.accountservice.service.account;

public interface IAccountDeletionService {

    /**
     * This method is used to request the deletion of an account.
     * It first checks if an account with the given ID exists.
     * If the account exists and is in the ACTIVE state, it sets the account status to PENDING_DELETE, locks the account, disables notifications, and sets a deletion date 30 days in the future.
     * It then saves the updated account and generates email tokens for account deletion.
     * If there's an unexpected error during the account update or email token generation, it throws a RuntimeException.
     * If the account is not in the ACTIVE state or does not exist, it logs the information and returns false.
     *
     * @param accountId the ID of the account to be deleted
     * @return true if the account deletion request is successful, false otherwise
     * @throws RuntimeException if there's an unexpected error during the account update or email token generation
     */
    boolean requestAccountDelete(int accountId);
}
