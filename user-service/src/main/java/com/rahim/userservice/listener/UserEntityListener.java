package com.rahim.userservice.listener;

import com.rahim.userservice.model.User;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class UserEntityListener {
    private static final Logger log = LoggerFactory.getLogger(UserEntityListener.class);

    @PrePersist
    public void logNewUser(User user) {
        log.info("Attempting to add new user with email: {}", user.getEmail());
    }

    @PreUpdate
    public void preUpdate(User user) {
        OffsetDateTime now = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        user.setUpdatedAt(now);
        log.info("Attempting to update user with ID: {}", user.getId());
    }

    @PreRemove
    public void preRemove(Object o) {

    }

    @PostLoad
    public void postLoad(Object o) {

    }

    @PostRemove
    public void postRemove(Object o) {

    }

    @PostUpdate
    public void postUpdate(User user) {

    }

    @PostPersist
    public void postPersist(Object o) {

    }
}
