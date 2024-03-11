package com.rahim.pricingservice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 01/12/2023
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GoldData {
    private String source;
    private BigDecimal price;
    private String requestDate;

    public GoldData(String data) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        GoldData goldData = mapper.readValue(data, GoldData.class);

        this.source = goldData.getSource();
        this.price = goldData.getPrice();
        this.requestDate = goldData.getRequestDate();

    }
}
