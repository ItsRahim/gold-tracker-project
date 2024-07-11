package com.rahim.common.constant;

/**
 * @author Rahim Ahmed
 * @created 04/05/2024
 */
public class HazelcastConstant {

    private HazelcastConstant() {
    }

    /*Set containing account ids with notifications enabled*/
    public static final String ACCOUNT_ID_NOTIFICATION_SET = "account-id-notification";

    /*Set containing all account ids on the system*/
    public static final String ACCOUNT_ID_SET = "account-ids";

    /*Set containing kafka messages which have been processed*/
    public static final String PROCESSED_KAFKA_MESSAGES = "processed-kafka-messages";

    /*Map containing hazelcast storage initialisation state from various microservices*/
    public static final String HAZELCAST_INITIALISER_MAP = "init-status";

    /*Map containing all gold type names and their id in the system*/
    public static final String GOLD_TYPE_MAP = "gold-types";
}
