package com.rahim.common.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 27/04/2024
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PriceAlertEmailData {
    private String firstName;
    private String lastName;
    private String email;
    private String thresholdPrice;
    private String alertDateTime;
    private String emailTemplate;

}
