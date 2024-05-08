package com.rahim.common.dao;

/**
 * @author Rahim Ahmed
 * @created 08/05/2024
 */
public class KafkaDataAccess {

    private KafkaDataAccess() {}

    public static final String TABLE_NAME = "rgts.kafka_unsent_messages";
    public static final String COL_ID = "id";
    public static final String COL_FAILED_TIMESTAMP = "failed_time";
    public static final String COL_TOPIC = "topic";
    public static final String COL_MESSAGE_DATA = "message_data";

}
