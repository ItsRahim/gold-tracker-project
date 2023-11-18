package com.rahim.userservice.service;

public interface IInternalUserService {
    void deleteUserAccount(int userId);
    void findAllInactiveUsers();
    void processPendingDeleteUsers();

    void processInactiveUsers();

}
