package com.rahim.common.service.hazelcast;

import com.rahim.common.model.HzPersistenceModel;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
public interface HzPersistenceService {
    void persistToDB(HzPersistenceModel persistenceModel);
    void removeFromDB(HzPersistenceModel persistenceModel);
}
