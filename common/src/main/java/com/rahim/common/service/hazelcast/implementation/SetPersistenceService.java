package com.rahim.common.service.hazelcast.implementation;

import com.rahim.common.dao.HzSetDataAccess;
import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.DuplicateEntityException;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.model.HzPersistenceModel;
import com.rahim.common.model.HzSetData;
import com.rahim.common.service.hazelcast.AbstractPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
@Primary
@Service("hzSetResilienceService")
public class SetPersistenceService extends AbstractPersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(SetPersistenceService.class);

    public SetPersistenceService(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
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
                LOG.debug("No rows found for deletion with set name: {} and set value: {}", setData.getSetName(), setData.getSetValue());
            } else if (rowsAffected != 1) {
                throw new DataIntegrityViolationException("Unexpected number of rows removed: " + rowsAffected);
            }

            LOG.debug("Successfully removed {} rows from the database for persistence model: {}", rowsAffected, persistenceModel);
        } catch (Exception e) {
            LOG.error("Error deleting data from database: {}", e.getMessage());
            throw new DatabaseException("Error deleting data from database");
        }
    }

    @Override
    protected String generateQuery(HzPersistenceModel persistenceModel) {
        try {
            HzSetData setData = persistenceModel.getSetData();
            validateSetData(setData);

            if (doesSetExist(setData.getSetName(), setData.getSetValue())) {
                LOG.warn("Set with name {} and value {} already exists", setData.getSetName(), setData.getSetValue());
                throw new DuplicateEntityException("Set with name " + setData.getSetName() + " and value " + setData.getSetValue() + " already exists");
            }

            return "INSERT INTO " + HzSetDataAccess.TABLE_NAME +
                    " (" +
                    HzSetDataAccess.COL_SET_NAME +
                    ", " +
                    HzSetDataAccess.COL_SET_VALUE +
                    ") VALUES (?, ?)";
        } catch (Exception ex) {
            LOG.warn("Error generating SQL query: {}", ex.getMessage());
            throw new RuntimeException("Error generating SQL query", ex);
        }
    }

    private void validateSetData(HzSetData setData) {
        if (setData == null || setData.getSetName() == null || setData.getSetValue() == null) {
            LOG.error("Invalid set data: {}", setData);
            throw new ValidationException("Invalid set data");
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

    @Override
    protected String[] generateParameters(HzPersistenceModel persistenceModel) {
        HzSetData setData = persistenceModel.getSetData();
        Object[] parameters = new Object[]{setData.getSetName(), setData.getSetValue()};

        String[] stringParameters = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            stringParameters[i] = parameters[i].toString();
        }

        return stringParameters;
    }

    @Override
    public void clearFromDB(HzPersistenceModel persistenceModel) {
        try {
            HzSetData setData = persistenceModel.getSetData();
            String setName = setData.getSetName().trim();
            if (setName.isBlank() || setName.isEmpty()) {
                LOG.error("Invalid set name");
                throw new IllegalArgumentException("Set name cannot be null or blank");
            }

            String query = "DELETE FROM "
                    + HzSetDataAccess.TABLE_NAME
                    + " WHERE "
                    + HzSetDataAccess.COL_SET_NAME
                    + " = ?";
            int rowsAffected = jdbcTemplate.update(query, setName);

            LOG.debug("Cleared data associated with set name '{}' from the database. Rows affected: {}", setData.getSetName(), rowsAffected);
        } catch (Exception e) {
            LOG.error("Error clearing data from database: {}", e.getMessage(), e);
            throw new DatabaseException("Error clearing data from database");
        }
    }

}
