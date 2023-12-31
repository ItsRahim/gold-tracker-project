package com.rahim.userservice.service.account;

import java.util.Map;

public interface IAccountUpdateService {
    void updateAccount(int userId, Map<String, String> updatedData) throws Exception;
}
