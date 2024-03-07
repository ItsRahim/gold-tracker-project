package com.rahim.accountservice.listener;

import com.rahim.accountservice.model.Profile;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A listener class that listens and logs all CRUD operations occurring with the profile table/entity
 *
 * @author Rahim Ahmed
 * @created 05/11/2023
 */
@Component
public class UserProfileEntityListener {

    private static final Logger LOG = LoggerFactory.getLogger(UserProfileEntityListener.class);

    @PrePersist
    public void prePersist(Profile profile) {
        LOG.debug("Server attempting to add new profile with ID: {}", profile.getId());
    }

    @PreUpdate
    public void preUpdate(Profile profile) {
        LOG.debug("Server attempting to update new profile with ID: {}", profile.getId());
    }

    @PreRemove
    public void preRemove(Profile profile) {
        LOG.debug("Server attempting to remove new profile with ID: {}", profile.getId());
    }

    @PostLoad
    public void postLoad(Profile profile) {
        LOG.debug("Server successfully loaded profile with ID: {}", profile.getId());
    }

    @PostRemove
    public void postRemove(Profile profile) {
        LOG.debug("Server successfully removed profile with ID: {}", profile.getId());
    }

    @PostUpdate
    public void postUpdate(Profile profile) {
        LOG.debug("Server successfully updated profile with ID: {}", profile.getId());
    }

    @PostPersist
    public void postPersist(Profile profile) {
        LOG.debug("Server successfully inserted profile with ID: {}", profile.getId());
    }
}
