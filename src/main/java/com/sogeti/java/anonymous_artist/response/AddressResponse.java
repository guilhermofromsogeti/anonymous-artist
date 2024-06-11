package com.sogeti.java.anonymous_artist.response;

public record AddressResponse(
        String message,
        String streetName,
        String houseNumber,
        String houseNumberAddition,
        String zipCode,
        String city,
        String province,
        String country
) {
}
