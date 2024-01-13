package com.rahim.accountservice.service.repository;

import com.rahim.accountservice.model.Account;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface IAccountRepositoryHandler {
    Optional<Account> findById(int accountId);
    void saveAccount(Account account);
    void deleteAccount(int accountId);
    boolean hasAccount(String email);
    List<Account> getInactiveUsers(LocalDate cutoffDate);
    List<Account> getUsersToDelete(LocalDate cutoffDate);
    List<Account> getPendingDeleteUsers();
    List<Account> getAllAccounts();
}
