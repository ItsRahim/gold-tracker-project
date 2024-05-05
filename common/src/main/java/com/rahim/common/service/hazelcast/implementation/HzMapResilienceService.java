package com.rahim.common.service.hazelcast.implementation;

import com.rahim.common.dao.HzMapDataAccess;
import com.rahim.common.model.HzMapData;
import com.rahim.common.model.HzPersistenceModel;
import com.rahim.common.service.hazelcast.HzResilienceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
@Primary
@RequiredArgsConstructor
@Service("hzMapResilienceService")
public class HzMapResilienceService implements HzResilienceService {

    private static final Logger LOG = LoggerFactory.getLogger(HzMapResilienceService.class);
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void persistToDB(HzPersistenceModel persistenceModel) {
        LOG.debug("Persisting Hazelcast map to database: {}", persistenceModel);

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
        //Not applicable to maps
    }

    private String generateQuery(HzPersistenceModel persistenceModel) {
        HzMapData mapData = persistenceModel.getMapData();
        validateMapData(mapData);

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        sqlBuilder.append(HzMapDataAccess.TABLE_NAME)
                .append(" (")
                .append(HzMapDataAccess.COL_MAP_NAME)
                .append(", ")
                .append(HzMapDataAccess.COL_MAP_KEY)
                .append(", ")
                .append(HzMapDataAccess.COL_MAP_VALUE)
                .append(") VALUES (?, ?, ?)");

        return sqlBuilder.toString();
    }

    private void validateMapData(HzMapData mapData) {
        if (mapData == null || mapData.getMapName() == null || mapData.getMapKey() == null || mapData.getMapValue() == null) {
            LOG.error("Invalid map data: {}", mapData);
            throw new IllegalArgumentException("Invalid map data: " + mapData);
        }
    }

    private String[] generateParameters(HzPersistenceModel persistenceModel) {
        HzMapData mapData = persistenceModel.getMapData();
        Object[] parameters = new Object[]{mapData.getMapName(), mapData.getMapKey(), mapData.getMapValue()};

        String[] stringParameters = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            stringParameters[i] = parameters[i].toString();
        }

        return stringParameters;
    }

}
