package com.rahim.userservice.service.account;

import com.rahim.userservice.model.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountQueryService {
    void existsById(String accountId);
    List<Account> getAllAccounts();
    Optional<Account> findAccountById(int accountId);
}
