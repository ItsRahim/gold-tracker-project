package com.rahim.accountservice.service.hazelcast;

import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
public interface CacheManager {

    HazelcastInstance getInstance();
    void addToSet(Object value, String setName);
    void removeFromSet(Object value, String setName);
    void clearSet(String setName);
    <T> ISet<T> getSet(String setName);
}
