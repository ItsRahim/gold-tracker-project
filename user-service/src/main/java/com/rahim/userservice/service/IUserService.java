package com.rahim.userservice.service;

import com.rahim.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    void save(User user);
    Optional<User> findById(int id);
    List<User> findAllUsers();
}
