package com.rahim.userservice.service.repository;

import com.rahim.userservice.model.Account;

import java.util.Optional;


public interface IAccountRepositoryHandler {
    Optional<Account> findById(int accountId);
    void saveAccount(Account account);
    void deleteAccount(int accountId);
}
