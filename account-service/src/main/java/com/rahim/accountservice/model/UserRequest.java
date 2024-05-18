package com.rahim.accountservice.model;

import com.rahim.accountservice.dto.AccountRequestDto;
import com.rahim.accountservice.dto.ProfileRequestDto;
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
    private AccountRequestDto account;
    private ProfileRequestDto profile;
}
