package com.rahim.accountservice.request.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rahim.accountservice.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 18/05/2024
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCreationRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("contactNumber")
    private String contactNumber;

    @JsonProperty("address")
    private Address address;
}
