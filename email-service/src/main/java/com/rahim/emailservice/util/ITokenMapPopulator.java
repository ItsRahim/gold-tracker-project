package com.rahim.emailservice.util;

import java.util.List;
import java.util.Map;

public interface ITokenMapPopulator {
    void populateTokenMap(List<String> placeholders, List<String> tokens);
    Map<String, String> getPlaceholderTokenMap();
}
