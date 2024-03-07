package com.rahim.accountservice.service.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahim.accountservice.model.Account;

/**
 * @author Rahim Ahmed
 * @created 05/11/2023
 */
@Deprecated
public interface IAuditLog {
    void initialise(Account oldAccount, Account newAccount, String action) throws JsonProcessingException;
}
