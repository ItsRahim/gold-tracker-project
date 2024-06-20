package com.rahim.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rahim.userservice.model.Address;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "street")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "postCode", column = @Column(name = "post_code")),
            @AttributeOverride(name = "country", column = @Column(name = "country"))
    })
    @JsonProperty("address")
    private Address address;

    public Profile(Account account, String username, String firstName, String lastName, String contactNumber, Address address) {
        this.account = account;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public boolean isValid() {
        return !(username == null || username.isEmpty() ||
                firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                contactNumber == null || contactNumber.isEmpty() ||
                address == null);
    }
}