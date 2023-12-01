package com.rahim.userservice.listener;

import com.rahim.userservice.model.User;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserEntityListener {
    private static final Logger LOG = LoggerFactory.getLogger(UserEntityListener.class);

    @PrePersist
    public void prePersist(User user) {
        LOG.debug("Server attempting to add new user with ID: {}", user.getId());
    }

    @PreUpdate
    public void preUpdate(User user) {
        LOG.debug("Server attempting to update new user with ID: {}", user.getId());
    }

    @PreRemove
    public void preRemove(User user) {
        LOG.debug("Server attempting to remove new user with ID: {}", user.getId());
    }

    @PostLoad
    public void postLoad(User user) {
        LOG.debug("Server successfully loaded user with ID: {}", user.getId());
    }

    @PostRemove
    public void postRemove(User user) {
        LOG.debug("Server successfully removed user with ID: {}", user.getId());
    }

    @PostUpdate
    public void postUpdate(User user) {
        LOG.debug("Server successfully updated user with ID: {}", user.getId());
    }

    @PostPersist
    public void postPersist(User user) {
        LOG.debug("Server successfully inserted user with ID: {}", user.getId());
    }
}
