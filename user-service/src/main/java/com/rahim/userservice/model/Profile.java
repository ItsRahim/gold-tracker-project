package com.rahim.userservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rahim.userservice.listener.UserProfileEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(UserProfileEntityListener.class)
@Table(name = "user_profiles", schema = "rgts")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false)
    @JsonProperty("id")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Account account;

    @Column(name = "username")
    @JsonProperty("username")
    private String username;

    @Column(name = "first_name")
    @JsonProperty("firstName")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("lastName")
    private String lastName;

    @Column(name = "contact_number")
    @JsonProperty("contactNumber")
    private String contactNumber;

    @Column(name = "address")
    @JsonProperty("address")
    private String address;
}