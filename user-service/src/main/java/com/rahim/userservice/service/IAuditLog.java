package com.rahim.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahim.userservice.model.User;

public interface IAuditLog {
    void initialise(User oldUser, User newUser, String action) throws JsonProcessingException;
}
