package com.rahim.common.model.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rahim.common.constant.EmailTemplate;
import lombok.*;

/**
 * @author Rahim Ahmed
 * @created 28/04/2024
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountEmailData {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String deleteDate;
    private String updatedAt;
    private EmailTemplate emailTemplate;
}
