package com.rahim.common.service.hazelcast;

import com.rahim.common.dao.HzMapDataAccess;
import com.rahim.common.dao.HzSetDataAccess;
import com.rahim.common.model.HzPersistenceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
public class HazelcastResilienceHandler {

    private HazelcastResilienceHandler() {}

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastResilienceHandler.class);

    public static void persistToDB(HzPersistenceModel persistenceModel) {
        LOG.debug("Persisting Hazelcast data to database: {}", persistenceModel.toString());

        StringBuilder sqlBuilder = new StringBuilder();
        if (!persistenceModel.getObjectOperation().equals(HzPersistenceModel.ObjectOperation.CREATE)) {
            LOG.error("Skipping database persistence for non-create operation: {}", persistenceModel);
            return;
        } else {
            sqlBuilder.append("INSERT INTO ");
        }

        if (persistenceModel.getObjectType().equals(HzPersistenceModel.ObjectType.MAP)) {
            sqlBuilder.append(HzMapDataAccess.TABLE_NAME)
                    .append(" (")
                    .append(HzMapDataAccess.COL_MAP_NAME)
                    .append(", ")
                    .append(HzMapDataAccess.COL_MAP_KEY)
                    .append(", ")
                    .append(HzMapDataAccess.COL_MAP_VALUE)
                    .append(") VALUES (")
                    .append("'").append(persistenceModel.getObjectName()).append("', ")
                    .append("'").append(persistenceModel.getObjectKey()).append("', ")
                    .append("'").append(persistenceModel.getObjectValue()).append("');");
        } else {
            sqlBuilder.append(HzSetDataAccess.TABLE_NAME);
        }
    }

    private boolean isValidData(HzPersistenceModel persistenceModel) {
        if (persistenceModel.getObjectType().equals(HzPersistenceModel.ObjectType.MAP)) {
            return !(persistenceModel.getObjectName().isEmpty() && persistenceModel.getObjectKey().isBlank() && persistenceModel.getObjectValue() == null);
        } else if (persistenceModel.getObjectType().equals(HzPersistenceModel.ObjectType.SET)) {
            return !(persistenceModel.getObjectName().isEmpty() && persistenceModel.getObjectValue() == null);
        }
        return false;
    }
}
