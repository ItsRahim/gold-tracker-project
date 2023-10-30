package com.rahim.userservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "rgts")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "notification_setting")
    private Integer notificationSetting;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "login_attempts")
    private Integer loginAttempts;

    @Column(name = "to_delete")
    private Boolean toDelete;

    @Column(name = "delete_date")
    private OffsetDateTime deleteDate;

    @Column(name = "account_status", nullable = false)
    private String accountStatus;

}