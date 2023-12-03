package com.rahim.pricingservice.listener;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GoldPriceHistoryEntityListener {
    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceHistoryEntityListener.class);

    @PrePersist
    public void prePersist(Object o) {
        LOG.debug("Entity will be persisted: {}", o);
    }

    @PreUpdate
    public void preUpdate(Object o) {
        LOG.debug("Entity will be updated: {}", o);
    }

    @PreRemove
    public void preRemove(Object o) {
        LOG.debug("Entity will be removed: {}", o);
    }

    @PostLoad
    public void postLoad(Object o) {
        LOG.debug("Entity has been loaded: {}", o);
    }

    @PostRemove
    public void postRemove(Object o) {
        LOG.debug("Entity has been removed: {}", o);
    }

    @PostUpdate
    public void postUpdate(Object o) {
        LOG.debug("Entity has been updated: {}", o);
    }

    @PostPersist
    public void postPersist(Object o) {
        LOG.debug("Entity has been persisted: {}", o);
    }
}
