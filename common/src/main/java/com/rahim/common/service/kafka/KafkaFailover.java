package com.rahim.common.service.kafka;

import com.rahim.common.dao.KafkaDataAccess;
import com.rahim.common.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaFailover {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaFailover.class);
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_QUERY = "INSERT INTO "
            + KafkaDataAccess.TABLE_NAME
            + " ("
            + KafkaDataAccess.COL_TOPIC
            + ", "
            + KafkaDataAccess.COL_MESSAGE_DATA
            + ") VALUES (?, to_json(?::text))";

    @Transactional(rollbackFor = Exception.class)
    public void persistToDb(String topic, String data) {
        try {
            jdbcTemplate.update(INSERT_QUERY, topic, data);
            LOG.debug("Successfully persisted failed Kafka message to database. Topic: {}", topic);
        } catch (Exception e) {
            LOG.error("An error occurred when persisting failed kafka message to database", e);
            throw new DatabaseException("An error occurred when persisting failed kafka message to database");
        }
    }
}