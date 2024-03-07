package com.rahim.accountservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Deprecated
@Getter
@Setter
@AllArgsConstructor
public class AuditLogData {
    private Account oldAccount;
    private Account newAccount;
    private String actionPerformed;
}
