package com.rahim.userservice.service.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahim.userservice.model.Account;

@Deprecated
public interface IAuditLog {
    void initialise(Account oldAccount, Account newAccount, String action) throws JsonProcessingException;
}
