package com.rahim.userservice.listener;

import com.rahim.userservice.model.User;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserEntityListener {
    private static final Logger log = LoggerFactory.getLogger(UserEntityListener.class);

    @PrePersist
    public void logNewUser(User user) {
        log.debug("Server attempting to add new user with ID: {}", user.getId());
    }

    @PreUpdate
    public void preUpdate(User user) {
        log.debug("Server attempting to update new user with ID: {}", user.getId());
    }

    @PreRemove
    public void preRemove(User user) {
        log.debug("Server attempting to remove new user with ID: {}", user.getId());
    }

    @PostLoad
    public void postLoad(User user) {
        log.debug("Server successfully loaded user with ID: {}", user.getId());
    }

    @PostRemove
    public void postRemove(User user) {
        log.debug("Server successfully removed user with ID: {}", user.getId());
    }

    @PostUpdate
    public void postUpdate(User user) {
        log.debug("Server successfully updated user with ID: {}", user.getId());
    }

    @PostPersist
    public void postPersist(User user) {
        log.debug("Server successfully inserted user with ID: {}", user.getId());
    }
}
