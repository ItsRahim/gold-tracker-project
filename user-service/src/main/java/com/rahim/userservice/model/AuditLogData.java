package com.rahim.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Deprecated
@Getter
@Setter
@AllArgsConstructor
public class AuditLogData {
    private Account oldAccount;
    private Account newAccount;
    private String actionPerformed;
}
