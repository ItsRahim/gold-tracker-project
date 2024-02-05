package com.rahim.accountservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rahim.accountservice.listener.UserEntityListener;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@Entity
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(UserEntityListener.class)
@Table(name = "user_accounts", schema = "rgts")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    @JsonProperty("accountId")
    private Integer id;

    @Column(name = "email", nullable = false)
    @JsonProperty("email")
    private String email;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "account_status")
    @JsonProperty("accountStatus")
    private String accountStatus;

    @Column(name = "account_locked")
    @JsonProperty("accountLocked")
    private Boolean accountLocked;

    @Column(name = "credentials_expired")
    @JsonProperty("credentialsExpired")
    private Boolean credentialsExpired;

    @Column(name = "last_login")
    @JsonProperty("lastLogin")
    private Instant lastLogin;

    @Column(name = "notification_setting")
    @JsonProperty("notificationSetting")
    private Boolean notificationSetting;

    @Column(name = "created_at")
    @JsonProperty("createdAt")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updatedAt")
    private OffsetDateTime updatedAt;

    @Column(name = "login_attempts")
    @JsonProperty("loginAttempts")
    private Integer loginAttempts;

    @Column(name = "delete_date")
    @JsonProperty("deleteDate")
    private LocalDate deleteDate;

    public Account(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }
}