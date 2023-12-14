package com.rahim.userservice.service;

import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.model.User;
import com.rahim.userservice.model.UserRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserService {
    void createUserAndProfile(UserRequest userRequest) throws Exception;
    Optional<User> findUserById(int id) throws Exception;
    List<User> findAllUsers();
    boolean deleteUserRequest(int id);
    void updateUser(int userId, Map<String, String> updatedData) throws Exception;
    void deleteUserAccount(int userId);
    Map<String, Object> getEmailToken(int userId, TemplateNameEnum templateName);
}
