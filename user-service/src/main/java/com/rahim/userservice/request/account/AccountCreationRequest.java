package com.rahim.userservice.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
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
public class AccountCreationRequest {

    @Email
    @JsonProperty("email")
    private String email;

    private String password;
}
