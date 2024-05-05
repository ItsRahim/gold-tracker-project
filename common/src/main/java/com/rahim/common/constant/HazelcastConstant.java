package com.rahim.common.constant;

/**
 * @author Rahim Ahmed
 * @created 04/05/2024
 */
public class HazelcastConstant {

    private HazelcastConstant() {}

    /**
     * ----------------------------------------
     * Sets
     * ----------------------------------------
     */
    /*Set containing account ids with notifications enabled*/
    public static final String ACCOUNT_ID_SET = "account-id";

    /*Set containing kafka messages which have been processed*/
    public static final String PROCESSED_KAFKA_MESSAGES = "processed-kafka-messages";

    /**
     * ----------------------------------------
     * Maps
     * ----------------------------------------
     */
    /*Map containing hazelcast storage initialisation state from various microservices*/
    public static final String HAZELCAST_INITIALISER_MAP = "init-status";
}
