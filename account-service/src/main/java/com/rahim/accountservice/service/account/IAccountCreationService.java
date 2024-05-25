package com.rahim.accountservice.service.account;

import com.rahim.accountservice.exception.DuplicateAccountException;
import com.rahim.accountservice.model.UserRequest;

/**
 * @author Rahim Ahmed
 * @created 13/01/2024
 */
public interface IAccountCreationService {

    /**
     * This method is used to create a new account and its associated profile.
     *
     * @param userRequest the UserRequest object containing the account and profile to be created
     * @return
     * @throws DuplicateAccountException if an account with the same email or username already exists
     */
    UserRequest createAccount(UserRequest userRequest);
}
