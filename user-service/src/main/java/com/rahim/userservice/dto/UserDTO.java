package com.rahim.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
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
    private OffsetDateTime deleteDate;
}