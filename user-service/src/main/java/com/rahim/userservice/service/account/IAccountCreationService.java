package com.rahim.userservice.service.account;

import com.rahim.userservice.model.UserRequest;

/**
 * @author Rahim Ahmed
 * @created 13/01/2024
 */
public interface IAccountCreationService {

    UserRequest createAccount(UserRequest userRequest);
}
