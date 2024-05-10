package com.rahim.common.service.hazelcast.implementation;

import com.rahim.common.dao.HzMapDataAccess;
import com.rahim.common.model.HzMapData;
import com.rahim.common.model.HzPersistenceModel;
import com.rahim.common.service.hazelcast.AbstractPersistenceService;
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
@Service("hzMapResilienceService")
public class MapPersistenceService extends AbstractPersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(MapPersistenceService.class);

    public MapPersistenceService(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void removeFromDB(HzPersistenceModel persistenceModel) {
        LOG.debug("Removing Hazelcast set from database: {}", persistenceModel);

        try {
            if (!persistenceModel.getObjectOperation().equals(HzPersistenceModel.ObjectOperation.DELETE) ||
                    !persistenceModel.getObjectType().equals(HzPersistenceModel.ObjectType.MAP)) {
                LOG.error("Skipping database deletion for non-delete operation/object type: {}", persistenceModel);
                return;
            }

            HzMapData mapData = persistenceModel.getMapData();
            String mapName = mapData.getMapName();
            String mapKey = mapData.getMapKey();

            if (mapName == null || mapKey == null || !mapKeyExists(mapName, mapKey)) {
                LOG.error("Skipping database deletion due to missing map name or map key: {}", persistenceModel);
                return;
            }

            String query = "DELETE FROM "
                    + HzMapDataAccess.TABLE_NAME
                    + " WHERE "
                    + HzMapDataAccess.COL_MAP_NAME
                    + " = ? AND "
                    + HzMapDataAccess.COL_MAP_KEY + " = ?";
            Object[] params = {mapName, mapKey};

            int rowsAffected = jdbcTemplate.update(query, params);
            if (rowsAffected == 0) {
                LOG.warn("No rows found for deletion with map name: {} and key value: {}", mapName, mapKey);
            } else if (rowsAffected != 1) {
                throw new IllegalStateException("Unexpected number of rows removed: " + rowsAffected);
            }

            LOG.debug("Successfully removed {} rows from the database for persistence model: {}", rowsAffected, persistenceModel);
        } catch (Exception e) {
            LOG.error("Error deleting data from database: {}", e.getMessage());
            throw new RuntimeException("Error deleting data from database", e);
        }
    }

    @Override
    protected String generateQuery(HzPersistenceModel persistenceModel) {
        HzMapData mapData = persistenceModel.getMapData();
        validateMapData(mapData);
        String mapName = mapData.getMapName();
        String mapKey = mapData.getMapKey();
        if (mapKeyExists(mapName, mapKey)) {
            LOG.error("Map with name {} and key {} already exists", mapName, mapKey);
            throw new IllegalArgumentException("Set with name " + mapName + " and value " + mapKey + " already exists");
        }

        return "INSERT INTO " + HzMapDataAccess.TABLE_NAME +
                " (" +
                HzMapDataAccess.COL_MAP_NAME +
                ", " +
                HzMapDataAccess.COL_MAP_KEY +
                ", " +
                HzMapDataAccess.COL_MAP_VALUE +
                ") VALUES (?, ?, ?)";
    }

    private boolean mapKeyExists(String mapName, String mapKey) {
        String query = "SELECT COUNT(*) FROM "
                + HzMapDataAccess.TABLE_NAME
                + " WHERE "
                + HzMapDataAccess.COL_MAP_NAME
                + " = ? AND "
                + HzMapDataAccess.COL_MAP_KEY
                + " = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, mapName, mapKey);

        return count != null && count > 0;
    }

    private void validateMapData(HzMapData mapData) {
        if (mapData == null || mapData.getMapName() == null || mapData.getMapKey() == null || mapData.getMapValue() == null) {
            LOG.error("Invalid map data: {}", mapData);
            throw new IllegalArgumentException("Invalid map data: " + mapData);
        }
    }

    @Override
    protected String[] generateParameters(HzPersistenceModel persistenceModel) {
        HzMapData mapData = persistenceModel.getMapData();
        Object[] parameters = new Object[]{mapData.getMapName(), mapData.getMapKey(), mapData.getMapValue()};

        String[] stringParameters = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            stringParameters[i] = parameters[i].toString();
        }

        return stringParameters;
    }

    @Override
    public void clearFromDB(HzPersistenceModel persistenceModel) {
        try {
            HzMapData mapData = persistenceModel.getMapData();
            String mapName = mapData.getMapName().trim();
            if (mapName.isBlank() || mapName.isEmpty()) {
                LOG.error("Invalid map name: {}", (Object) null);
                throw new IllegalArgumentException("Map name cannot be null or blank");
            }

            String query = "DELETE FROM "
                    + HzMapDataAccess.TABLE_NAME
                    + " WHERE "
                    + HzMapDataAccess.COL_MAP_NAME
                    + " = ?";
            int rowsAffected = jdbcTemplate.update(query, mapName);

            LOG.debug("Cleared data associated with map name '{}' from the database. Rows affected: {}", mapName, rowsAffected);
        } catch (Exception e) {
            LOG.error("Error clearing data from database: {}", e.getMessage());
            throw new RuntimeException("Error clearing data from database", e);
        }
    }

}
