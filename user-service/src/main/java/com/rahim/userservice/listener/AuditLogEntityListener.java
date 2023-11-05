package com.rahim.userservice.listener;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class AuditLogEntityListener {
    private static final Logger log = LoggerFactory.getLogger(AuditLogEntityListener.class);

    @PrePersist
    public void prePersist(Object o) {
        log.info("Entity will be persisted: {}", o);
    }

    @PreUpdate
    public void preUpdate(Object o) {
        log.info("Entity will be updated: {}", o);
    }

    @PreRemove
    public void preRemove(Object o) {
        log.info("Entity will be removed: {}", o);
    }

    @PostLoad
    public void postLoad(Object o) {
        log.info("Entity has been loaded: {}", o);
    }

    @PostRemove
    public void postRemove(Object o) {
        log.info("Entity has been removed: {}", o);
    }

    @PostUpdate
    public void postUpdate(Object o) {
        log.info("Entity has been updated: {}", o);
    }

    @PostPersist
    public void postPersist(Object o) {
        log.info("Entity has been persisted: {}", o);
    }
}