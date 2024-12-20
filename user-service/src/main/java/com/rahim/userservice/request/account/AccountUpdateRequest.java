package com.rahim.userservice.request.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 03/06/2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateRequest {
    private String email;
    private String password;
    private String notificationSetting;
}
