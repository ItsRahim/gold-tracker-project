package com.rahim.accountservice.model;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * @author Rahim Ahmed
 * @created 03/06/2024
 */
@Getter
@Setter
@Embeddable
public class Address {
    private String street;
    private String city;
    private String postCode;
    private String country;
}
