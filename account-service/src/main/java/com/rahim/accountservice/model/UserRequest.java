package com.rahim.accountservice.model;

import com.rahim.accountservice.request.AccountCreationRequest;
import com.rahim.accountservice.request.ProfileCreationRequest;
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
