package com.rahim.userservice.model;

import com.rahim.userservice.request.account.AccountCreationRequest;
import com.rahim.userservice.request.profile.ProfileCreationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 30/10/2023
 */
@Getter
@Setter
@AllArgsConstructor
public class UserRequest {
    private AccountCreationRequest account;
    private ProfileCreationRequest profile;
}
