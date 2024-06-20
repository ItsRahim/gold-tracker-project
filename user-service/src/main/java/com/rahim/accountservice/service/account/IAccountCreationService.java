package com.rahim.accountservice.service.account;

import com.rahim.accountservice.model.UserRequest;

/**
 * @author Rahim Ahmed
 * @created 13/01/2024
 */
public interface IAccountCreationService {

    UserRequest createAccount(UserRequest userRequest);
}
