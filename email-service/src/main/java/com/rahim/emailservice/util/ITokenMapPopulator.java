package com.rahim.emailservice.util;

import java.util.List;
import java.util.Map;

/**
 * This class is a utility that populates a map with placeholders and tokens.
 * It implements the ITokenMapPopulator interface.
 * It uses a ConcurrentHashMap to store the placeholder-token pairs, ensuring thread safety.
 *
 * @author Rahim Ahmed
 * @created 12/12/2023
 */
public interface ITokenMapPopulator {

    /**
     * This method populates the token map with the provided placeholders and tokens.
     * It iterates over the placeholders and tokens, and puts each placeholder-token pair into the map.
     *
     * @param placeholders the placeholders to put into the map
     * @param tokens the tokens to put into the map
     */
    void populateTokenMap(List<String> placeholders, List<String> tokens);

    /**
     * This method returns a copy of the placeholder-token map.
     * It creates a new HashMap with the contents of the placeholder-token map.
     *
     * @return a copy of the placeholder-token map
     */
    Map<String, String> getPlaceholderTokenMap();
}
