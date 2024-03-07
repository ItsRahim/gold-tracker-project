package com.rahim.accountservice.listener;

import com.rahim.accountservice.model.Account;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A listener class that listens and logs all CRUD operations occurring with the account table/entity
 *
 * @author Rahim Ahmed
 * @created 05/11/2023
 */
@Component
public class AccountEntityListener {

    private static final Logger LOG = LoggerFactory.getLogger(AccountEntityListener.class);

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
