package com.rahim.common.model.kafka;

import com.rahim.common.constant.EmailTemplate;
import lombok.*;

/**
 * @author Rahim Ahmed
 * @created 27/04/2024
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceAlertEmailData {
    private String firstName;
    private String lastName;
    private String email;
    private String thresholdPrice;
    private String alertDateTime;
    private EmailTemplate emailTemplate;

}
