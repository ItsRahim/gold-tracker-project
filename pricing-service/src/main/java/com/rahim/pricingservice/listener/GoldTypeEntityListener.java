package com.rahim.pricingservice.listener;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 03/12/2023
 */
@Component
@RequiredArgsConstructor
public class GoldTypeEntityListener {
    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeEntityListener.class);

    @PrePersist
    public void prePersist(Object o) {
        LOG.trace("Entity will be persisted: {}", o);
    }

    @PreUpdate
    public void preUpdate(Object o) {
        LOG.trace("Entity will be updated: {}", o);
    }

    @PreRemove
    public void preRemove(Object o) {
        LOG.trace("Entity will be removed: {}", o);
    }

    @PostLoad
    public void postLoad(Object o) {
        LOG.trace("Entity has been loaded: {}", o);
    }

    @PostRemove
    public void postRemove(Object o) {
        LOG.trace("Entity has been removed: {}", o);
    }

    @PostUpdate
    public void postUpdate(Object o) {
        LOG.trace("Entity has been updated: {}", o);
    }

    @PostPersist
    public void postPersist(Object o) {
        LOG.trace("Entity has been persisted: {}", o);
    }
}
