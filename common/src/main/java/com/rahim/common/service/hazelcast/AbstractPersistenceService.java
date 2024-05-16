package com.rahim.common.service.hazelcast;

import com.rahim.common.model.HzPersistenceModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rahim Ahmed
 * @created 06/05/2024
 */
@RequiredArgsConstructor
public abstract class AbstractPersistenceService implements HzPersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPersistenceService.class);
    protected final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void persistToDB(HzPersistenceModel persistenceModel) {
        LOG.debug("Persisting to database: {}", persistenceModel);
        try {
            if (persistenceModel.getObjectOperation() != HzPersistenceModel.ObjectOperation.CREATE) {
                LOG.warn("Skipping database persistence for non-create operation: {}", persistenceModel);
                return;
            }

            String query = generateQuery(persistenceModel);
            Object[] params = generateParameters(persistenceModel);
            jdbcTemplate.update(query, params);
            LOG.debug("Persisted {} to database", persistenceModel);

        } catch (Exception e) {
            LOG.warn("Error persisting data to database: {}", e.getMessage());
        }
    }

    protected abstract String generateQuery(HzPersistenceModel persistenceModel);
    protected abstract Object[] generateParameters(HzPersistenceModel persistenceModel);
}
