package com.rahim.accountservice.service.account;

import java.util.Map;

public interface IAccountUpdateService {
    void updateAccount(int userId, Map<String, String> updatedData) throws Exception;
}
