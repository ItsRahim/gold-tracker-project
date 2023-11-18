package com.rahim.userservice.service;

public interface IInternalUserService {
    void deleteUserAccount(int userId);
    void processPendingDeleteUsers();
    void findAllInactiveUsers();
    void processInactiveUsers();

}
