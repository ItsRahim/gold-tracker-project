package com.rahim.userservice.model;

import com.rahim.userservice.listener.UserEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(UserEntityListener.class)
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

    @Column(name = "account_status")
    private String accountStatus;

    @Column(name = "account_locked")
    private Boolean accountLocked;

    @Column(name = "credentials_expired")
    private Boolean credentialsExpired;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "notification_setting")
    private Boolean notificationSetting;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "login_attempts")
    private Integer loginAttempts;

    @Column(name = "delete_date")
    private OffsetDateTime deleteDate;

    public User(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.passwordHash = user.getPasswordHash();
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