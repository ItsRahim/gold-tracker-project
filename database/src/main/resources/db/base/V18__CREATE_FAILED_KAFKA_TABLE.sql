CREATE TABLE rgts.kafka_unsent_messages  (
    id SERIAL PRIMARY KEY,
    failed_time TIMESTAMP WITH TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC'),
    topic VARCHAR(255),
    message_data JSONB
);

COMMENT ON TABLE rgts.kafka_unsent_messages IS 'A table to store unsent Kafka messages';
COMMENT ON COLUMN rgts.kafka_unsent_messages.failed_time IS 'The time kafka message failed to send';
COMMENT ON COLUMN rgts.kafka_unsent_messages.topic IS 'The name of the Kafka topic to which the message was supposed to be sent';
COMMENT ON COLUMN rgts.kafka_unsent_messages.message_data IS 'The actual data of the Kafka message';