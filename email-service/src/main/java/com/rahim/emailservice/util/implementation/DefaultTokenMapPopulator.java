package com.rahim.emailservice.util.implementation;

import com.rahim.emailservice.util.ITokenMapPopulator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is a utility that populates a map with placeholders and tokens.
 * It implements the ITokenMapPopulator interface.
 * It uses a ConcurrentHashMap to store the placeholder-token pairs, ensuring thread safety.
 *
 * @author Rahim Ahmed
 * @created 12/12/2023
 */
@Getter
@Component
@RequiredArgsConstructor
public class DefaultTokenMapPopulator implements ITokenMapPopulator {

    private static final Map<String, String> placeholderTokenMap = new ConcurrentHashMap<>();

    @Override
    public void populateTokenMap(List<String> placeholders, List<String> tokens) {
        for (int i = 0; i < placeholders.size(); i++) {
            placeholderTokenMap.put(placeholders.get(i), tokens.get(i));
        }
    }

    @Override
    public Map<String, String> getPlaceholderTokenMap() {
        return new HashMap<>(placeholderTokenMap);
    }
}