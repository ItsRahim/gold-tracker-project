package com.rahim.emailservice.util.implementation;

import com.rahim.emailservice.util.ITokenMapPopulator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@RequiredArgsConstructor
public class DefaultTokenMapPopulator implements ITokenMapPopulator {

    private static final Map<String, String> placeholderTokenMap = new HashMap<>();

    @Override
    public void populateTokenMap(List<String> placeholders, List<String> tokens) {
        for (int i = 0; i < placeholders.size(); i++) {
            placeholderTokenMap.put(placeholders.get(i), tokens.get(i));
        }
    }

    public Map<String, String> getPlaceholderTokenMap() {
        return new HashMap<>(placeholderTokenMap);
    }
}