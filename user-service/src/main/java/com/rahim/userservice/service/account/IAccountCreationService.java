package com.rahim.userservice.service.account;

import com.rahim.common.exception.DuplicateEntityException;
import com.rahim.common.exception.ValidationException;
import com.rahim.userservice.model.UserRequest;

/**
 * @author Rahim Ahmed
 * @created 13/01/2024
 */
public interface IAccountCreationService {

    /**
     * Creates a new user account based on the provided userRequest
     *
     * @param userRequest the user request containing details to create the account and profile
     * @return {@link UserRequest} object with details of the new account created
     * @throws DuplicateEntityException if a user with same username and/or email exists
     * @throws ValidationException      if the request data provided is invalid
     */
    UserRequest createAccount(UserRequest userRequest);
}
