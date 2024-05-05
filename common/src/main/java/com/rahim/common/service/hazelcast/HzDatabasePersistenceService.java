package com.rahim.common.service.hazelcast;

import com.rahim.common.dao.HzMapDataAccess;
import com.rahim.common.dao.HzSetDataAccess;
import com.rahim.common.model.HzMapData;
import com.rahim.common.model.HzPersistenceModel;
import com.rahim.common.model.HzSetData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HzDatabasePersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(HzDatabasePersistenceService.class);
    private final JdbcTemplate jdbcTemplate;

    public void persistToDB(HzPersistenceModel persistenceModel) {
        LOG.debug("Persisting Hazelcast data to database: {}", persistenceModel);

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

    private Object[] generateParameters(HzPersistenceModel persistenceModel) {
        if (persistenceModel.getObjectType() == HzPersistenceModel.ObjectType.MAP) {
            HzMapData mapData = persistenceModel.getMapData();
            return new Object[]{mapData.getMapName(), mapData.getMapKey(), mapData.getMapValue()};
        } else {
            HzSetData setData = persistenceModel.getSetData();
            return new Object[]{setData.getSetName(), setData.getSetValue()};
        }
    }

    private String generateQuery(HzPersistenceModel persistenceModel) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        if (persistenceModel.getObjectType() == HzPersistenceModel.ObjectType.MAP) {
            HzMapData mapData = persistenceModel.getMapData();
            validateMapData(mapData);
            sqlBuilder.append(HzMapDataAccess.TABLE_NAME)
                    .append(" (")
                    .append(HzMapDataAccess.COL_MAP_NAME)
                    .append(", ")
                    .append(HzMapDataAccess.COL_MAP_KEY)
                    .append(", ")
                    .append(HzMapDataAccess.COL_MAP_VALUE)
                    .append(") VALUES (?, ?, ?)");
        } else {
            HzSetData setData = persistenceModel.getSetData();
            validateSetData(setData);
            sqlBuilder.append(HzSetDataAccess.TABLE_NAME)
                    .append(" (")
                    .append(HzSetDataAccess.COL_SET_NAME)
                    .append(", ")
                    .append(HzSetDataAccess.COL_SET_VALUE)
                    .append(") VALUES (?, ?)");
        }
        return sqlBuilder.toString();
    }

    private void validateMapData(HzMapData mapData) {
        if (mapData == null || mapData.getMapName() == null || mapData.getMapKey() == null || mapData.getMapValue() == null) {
            LOG.error("Invalid map data: {}", mapData);
            throw new IllegalArgumentException("Invalid map data: " + mapData);
        }
    }

    private void validateSetData(HzSetData setData) {
        if (setData == null || setData.getSetName() == null || setData.getSetValue() == null) {
            LOG.error("Invalid set data: {}", setData);
            throw new IllegalArgumentException("Invalid set data: " + setData);
        }
    }
}
