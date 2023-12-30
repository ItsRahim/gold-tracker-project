package com.rahim.userservice.dto;

import com.rahim.userservice.model.Account;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String email;
    private String accountStatus;
    private Boolean accountLocked;
    private Boolean credentialsExpired;
    private Instant lastLogin;
    private Boolean notificationSetting;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer loginAttempts;
    private LocalDate deleteDate;

    public UserDTO(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.accountStatus = account.getAccountStatus();
        this.accountLocked = account.getAccountLocked();
        this.credentialsExpired = account.getCredentialsExpired();
        this.lastLogin = account.getLastLogin();
        this.notificationSetting = account.getNotificationSetting();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
        this.loginAttempts = account.getLoginAttempts();
        this.deleteDate = account.getDeleteDate();
    }
}