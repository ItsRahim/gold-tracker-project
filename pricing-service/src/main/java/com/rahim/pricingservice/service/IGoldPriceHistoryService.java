package com.rahim.pricingservice.service;

import java.math.BigDecimal;

public interface IGoldPriceHistoryService {
    void updateHistoryTable(BigDecimal pricePerOunce);
}
