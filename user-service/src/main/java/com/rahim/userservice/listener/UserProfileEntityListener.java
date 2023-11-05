package com.rahim.userservice.listener;

import com.rahim.userservice.model.UserProfile;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserProfileEntityListener {
    private static final Logger log = LoggerFactory.getLogger(UserProfileEntityListener.class);

    @PrePersist
    public void prePersist(UserProfile profile) {
        log.debug("Server attempting to add new profile with ID: {}", profile.getId());
    }

    @PreUpdate
    public void preUpdate(UserProfile profile) {
        log.debug("Server attempting to update new profile with ID: {}", profile.getId());
    }

    @PreRemove
    public void preRemove(UserProfile profile) {
        log.debug("Server attempting to remove new profile with ID: {}", profile.getId());
    }

    @PostLoad
    public void postLoad(UserProfile profile) {
        log.debug("Server successfully loaded profile with ID: {}", profile.getId());
    }

    @PostRemove
    public void postRemove(UserProfile profile) {
        log.debug("Server successfully removed profile with ID: {}", profile.getId());
    }

    @PostUpdate
    public void postUpdate(UserProfile profile) {
        log.debug("Server successfully updated profile with ID: {}", profile.getId());
    }

    @PostPersist
    public void postPersist(UserProfile profile) {
        log.debug("Server successfully inserted profile with ID: {}", profile.getId());
    }
}
