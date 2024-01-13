package com.rahim.accountservice.service.account;

public interface IAccountDeletionService {
    void deleteAccount(int accountId);
    boolean requestAccountDelete(int accountId);
}
