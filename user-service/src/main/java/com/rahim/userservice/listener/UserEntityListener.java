package com.rahim.userservice.listener;

import com.rahim.userservice.model.Account;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserEntityListener {
    private static final Logger LOG = LoggerFactory.getLogger(UserEntityListener.class);

    @PrePersist
    public void prePersist(Account account) {
        LOG.debug("Server attempting to add new account with ID: {}", account.getId());
    }

    @PreUpdate
    public void preUpdate(Account account) {
        LOG.debug("Server attempting to update new account with ID: {}", account.getId());
    }

    @PreRemove
    public void preRemove(Account account) {
        LOG.debug("Server attempting to remove new account with ID: {}", account.getId());
    }

    @PostLoad
    public void postLoad(Account account) {
        LOG.debug("Server successfully loaded account with ID: {}", account.getId());
    }

    @PostRemove
    public void postRemove(Account account) {
        LOG.debug("Server successfully removed account with ID: {}", account.getId());
    }

    @PostUpdate
    public void postUpdate(Account account) {
        LOG.debug("Server successfully updated account with ID: {}", account.getId());
    }

    @PostPersist
    public void postPersist(Account account) {
        LOG.debug("Server successfully inserted account with ID: {}", account.getId());
    }
}
