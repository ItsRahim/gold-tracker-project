package com.rahim.common.service.hazelcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
public class HazelcastResilienceHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HazelcastResilienceHandler.class);

    public static void persistToDB() {
        LOG.debug("Persisting Hazelcast data to database");
    }
}
