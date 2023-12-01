package com.rahim.pricingservice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoldData {
    private String source;
    private double price;
    private String requestDate;

    public GoldData(String data) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        GoldData goldData = mapper.readValue(data, GoldData.class);

        this.source = goldData.getSource();
        this.price = goldData.getPrice();
        this.requestDate = goldData.getRequestDate();

    }
}
