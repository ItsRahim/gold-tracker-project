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
        //Not applicable to maps
    }

    @Override
    protected String generateQuery(HzPersistenceModel persistenceModel) {
        HzMapData mapData = persistenceModel.getMapData();
        validateMapData(mapData);

        return "INSERT INTO " + HzMapDataAccess.TABLE_NAME +
                " (" +
                HzMapDataAccess.COL_MAP_NAME +
                ", " +
                HzMapDataAccess.COL_MAP_KEY +
                ", " +
                HzMapDataAccess.COL_MAP_VALUE +
                ") VALUES (?, ?, ?)";
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

}
