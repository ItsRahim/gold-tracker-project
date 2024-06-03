package com.rahim.accountservice.request.account;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 03/06/2024
 */
@Getter
@Setter
public class AccountUpdateRequest {
    private String email;
    private String passwordHash;
    private String notificationSetting;
}
