package com.rahim.accountservice.request.profile;

import com.rahim.accountservice.model.Address;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rahim Ahmed
 * @created 03/06/2024
 */
@Getter
@Setter
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String contactNumber;
    private Address address;
}
