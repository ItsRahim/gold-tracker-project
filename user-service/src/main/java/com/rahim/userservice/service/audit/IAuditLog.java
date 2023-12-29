package com.rahim.userservice.service.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahim.userservice.model.User;

@Deprecated
public interface IAuditLog {
    void initialise(User oldUser, User newUser, String action) throws JsonProcessingException;
}
