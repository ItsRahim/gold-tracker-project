package com.rahim.userservice.dto;

import com.rahim.userservice.model.User;
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

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.accountStatus = user.getAccountStatus();
        this.accountLocked = user.getAccountLocked();
        this.credentialsExpired = user.getCredentialsExpired();
        this.lastLogin = user.getLastLogin();
        this.notificationSetting = user.getNotificationSetting();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.loginAttempts = user.getLoginAttempts();
        this.deleteDate = user.getDeleteDate();
    }
}