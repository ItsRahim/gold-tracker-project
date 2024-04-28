package com.rahim.accountservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

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
public class EmailToken {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String deleteDate;
    private String updatedAt;
    private String emailTemplate;
}
