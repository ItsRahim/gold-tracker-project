package com.rahim.userservice.service.account;

import com.rahim.userservice.model.Account;
import com.rahim.userservice.model.UserRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserService {
    void createUserAndProfile(UserRequest userRequest) throws Exception;
    Optional<Account> findUserById(int id) throws Exception;
    List<Account> findAllUsers();
    boolean deleteUserRequest(int id);
    void updateUser(int userId, Map<String, String> updatedData) throws Exception;
    void deleteUserAccount(int userId);
    void existsById(String userId);
}
