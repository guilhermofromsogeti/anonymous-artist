package com.sogeti.java.anonymous_artist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;
    private String streetName;
    private String houseNumber;
    private String houseNumberAddition;
    private String zipCode;
    private String city;
    private String province;
    private String country;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "membership_id")
    private User user;

    public Address(String streetName, String houseNumber, String houseNumberAddition, String zipCode, String city,
                   String province, String country) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.houseNumberAddition = houseNumberAddition;
        this.zipCode = zipCode;
        this.city = city;
        this.province = province;
        this.country = country;
    }
}
