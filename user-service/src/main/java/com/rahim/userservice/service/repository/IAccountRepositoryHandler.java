package com.rahim.userservice.service.repository;

import com.rahim.userservice.model.User;

import java.util.Optional;


public interface IUserRepositoryHandler {
    Optional<User> findById(int userId);
    void saveUserAccount(User user);
    void deleteUserAccount(int userId);
}
