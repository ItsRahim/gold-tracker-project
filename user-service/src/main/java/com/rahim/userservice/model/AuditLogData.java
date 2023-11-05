package com.rahim.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuditLogData {
    private User oldUser;
    private User newUser;
    private String actionPerformed;
}
