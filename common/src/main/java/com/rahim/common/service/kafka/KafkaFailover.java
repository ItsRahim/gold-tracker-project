package com.rahim.common.service.kafka;

import com.rahim.common.dao.KafkaDataAccess;
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

    @Transactional
    public void persistToDb(String topic, String data) {
        try {
            String query = "INSERT INTO "
                    + KafkaDataAccess.TABLE_NAME
                    + " ( "
                    + KafkaDataAccess.COL_TOPIC
                    + ", "
                    + KafkaDataAccess.COL_MESSAGE_DATA
                    + ") VALUES (?, to_json(?::text))";
            jdbcTemplate.update(query, topic, data);
            LOG.debug("Successfully persisted failed Kafka message to database. Topic: {}", topic);
        } catch (Exception e) {
            LOG.error("An error occurred when persisting failed kafka message to database", e);
        }
    }
}