package com.rahim.accountservice.model;

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
    private Account account;
    private Profile profile;
}
