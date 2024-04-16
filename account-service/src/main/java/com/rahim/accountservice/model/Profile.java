package com.rahim.accountservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rahim Ahmed
 * @created 29/10/2023
 */
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "user_profiles", schema = "rgts")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false)
    @JsonProperty("id")
    private Integer id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
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

    public Profile(Account account, String username, String firstName, String lastName, String contactNumber, String address) {
        this.account = account;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.address = address;
    }
}