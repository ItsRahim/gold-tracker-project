package com.rahim.accountservice.service.account;

import com.rahim.accountservice.exception.DuplicateAccountException;
import com.rahim.accountservice.model.UserRequest;

public interface IAccountCreationService {

    /**
     * This method is used to create a new account and its associated profile.
     * It first validates the input account and profile, then checks if an account with the same email or username already exists.
     * If an account with the same email or username exists, it throws a DuplicateAccountException.
     * If no such account exists, it saves the account and creates the profile.
     * If there's an unexpected error during the account or profile creation, it throws a RuntimeException.
     *
     * @param userRequest the UserRequest object containing the account and profile to be created
     * @throws DuplicateAccountException if an account with the same email or username already exists
     */
    void createAccount(UserRequest userRequest);
}
