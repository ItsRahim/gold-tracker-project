package com.rahim.common.service.hazelcast.implementation;

import com.rahim.common.dao.HzSetDataAccess;
import com.rahim.common.model.HzPersistenceModel;
import com.rahim.common.model.HzSetData;
import com.rahim.common.service.hazelcast.HzPersistenceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
@Primary
@RequiredArgsConstructor
@Service("hzSetResilienceService")
public class HzSetPersistenceService implements HzPersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(HzSetPersistenceService.class);
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void persistToDB(HzPersistenceModel persistenceModel) {
        LOG.debug("Persisting Hazelcast set to database: {}", persistenceModel);
        try {
            if (persistenceModel.getObjectOperation() != HzPersistenceModel.ObjectOperation.CREATE) {
                LOG.error("Skipping database persistence for non-create operation: {}", persistenceModel);
                return;
            }

            String query = generateQuery(persistenceModel);
            Object[] params = generateParameters(persistenceModel);
            jdbcTemplate.update(query, params);
            LOG.debug("Persisted {} to database", persistenceModel);

        } catch (Exception e) {
            LOG.error("Error persisting data to database: {}", e.getMessage());
        }

    }

    @Override
    public void removeFromDB(HzPersistenceModel persistenceModel) {
        LOG.debug("Removing Hazelcast set from database: {}", persistenceModel);

        try {
            if (persistenceModel.getObjectOperation() != HzPersistenceModel.ObjectOperation.DELETE || persistenceModel.getObjectType() != HzPersistenceModel.ObjectType.SET) {
                LOG.error("Skipping database deletion for non-delete operation/object type: {}", persistenceModel);
                return;
            }

            HzSetData setData = persistenceModel.getSetData();
            if (setData.getSetName() == null || setData.getSetValue() == null) {
                LOG.error("Skipping database deletion due to missing set name or set value: {}", persistenceModel);
                return;
            }

            String query = "DELETE FROM "
                    + HzSetDataAccess.TABLE_NAME
                    + " WHERE "
                    + HzSetDataAccess.COL_SET_NAME
                    + " = ? AND "
                    + HzSetDataAccess.COL_SET_VALUE + " = ?";
            Object[] params = {setData.getSetName(), setData.getSetValue().toString()};

            int rowsAffected = jdbcTemplate.update(query, params);
            if (rowsAffected == 0) {
                LOG.warn("No rows found for deletion with set name: {} and set value: {}", setData.getSetName(), setData.getSetValue());
            } else if (rowsAffected != 1) {
                throw new IllegalStateException("Unexpected number of rows removed: " + rowsAffected);
            }

            LOG.debug("Successfully removed {} rows from the database for persistence model: {}", rowsAffected, persistenceModel);
        } catch (Exception e) {
            LOG.error("Error deleting data from database: {}", e.getMessage());
            throw new RuntimeException("Error deleting data from database", e);
        }
    }

    private String generateQuery(HzPersistenceModel persistenceModel) {
        try {
            HzSetData setData = persistenceModel.getSetData();
            validateSetData(setData);

            if (doesSetExist(setData.getSetName(), setData.getSetValue())) {
                LOG.error("Set with name {} and value {} already exists", setData.getSetName(), setData.getSetValue());
                throw new IllegalArgumentException("Set with name " + setData.getSetName() + " and value " + setData.getSetValue() + " already exists");
            }
            StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
            sqlBuilder.append(HzSetDataAccess.TABLE_NAME)
                    .append(" (")
                    .append(HzSetDataAccess.COL_SET_NAME)
                    .append(", ")
                    .append(HzSetDataAccess.COL_SET_VALUE)
                    .append(") VALUES (?, ?)");

            return sqlBuilder.toString();
        } catch (Exception ex) {
            LOG.error("Error generating SQL query: {}", ex.getMessage());
            throw new RuntimeException("Error generating SQL query", ex);
        }
    }

    private void validateSetData(HzSetData setData) {
        if (setData == null || setData.getSetName() == null || setData.getSetValue() == null) {
            LOG.error("Invalid set data: {}", setData);
            throw new IllegalArgumentException("Invalid set data: " + setData);
        }
    }

    private boolean doesSetExist(String setName, Object setValue) {
        String query = "SELECT COUNT(*) FROM "
                + HzSetDataAccess.TABLE_NAME
                + " WHERE "
                + HzSetDataAccess.COL_SET_NAME
                + " = ? AND "
                + HzSetDataAccess.COL_SET_VALUE
                + " = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, setName, setValue.toString());

        return count != null && count > 0;
    }

    private String[] generateParameters(HzPersistenceModel persistenceModel) {
        HzSetData setData = persistenceModel.getSetData();
        Object[] parameters = new Object[]{setData.getSetName(), setData.getSetValue()};

        String[] stringParameters = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            stringParameters[i] = parameters[i].toString();
        }

        return stringParameters;
    }


}
