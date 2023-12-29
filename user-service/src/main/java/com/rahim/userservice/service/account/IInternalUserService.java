package com.rahim.userservice.service.account;

public interface IInternalUserService {
    void deleteUserAccount(int userId);
    void runCleanupJob();
}