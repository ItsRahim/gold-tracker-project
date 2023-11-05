package com.rahim.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String address;
}