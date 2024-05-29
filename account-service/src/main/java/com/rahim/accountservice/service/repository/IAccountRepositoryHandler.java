package com.rahim.accountservice.service.repository;

import com.rahim.accountservice.model.Account;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * This interface class is responsible for CRUD operations on the Account table
 * This utilised by classes and methods within account-service <strong>only</strong>
 *
 * @author Rahim Ahmed
 * @created 29/12/2023
 */
public interface IAccountRepositoryHandler {

    Account findById(int accountId);
    void saveAccount(Account account);
    void deleteAccount(int accountId);
    boolean existsByEmail(String email);
    List<Account> getInactiveUsers(LocalDate cutoffDate);
    List<Integer> getUsersToDelete(LocalDate cutoffDate);
    List<Integer> getUsersPendingDeletion(LocalDate deletionDate);
    List<Account> getAllAccounts();
    Instant getUpdatedAtByUserId(Integer userId);
    List<Integer> getAccountActiveNotification();
    List<Integer> getAllAccountIds();

}
