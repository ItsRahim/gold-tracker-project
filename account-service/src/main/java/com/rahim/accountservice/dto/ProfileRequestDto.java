package com.rahim.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 18/05/2024
 */
@Getter
@Setter
@AllArgsConstructor
public class ProfileRequestDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("contactNumber")
    private String contactNumber;

    @JsonProperty("address")
    private String address;
}
