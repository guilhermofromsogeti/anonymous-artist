package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.request.AddressRequest;
import com.sogeti.java.anonymous_artist.response.AddressResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressMapper {
    public static Address fromAddressRequest(AddressRequest addressRequest) {
        return new Address(
                addressRequest.streetName(),
                addressRequest.houseNumber(),
                addressRequest.houseNumberAddition(),
                addressRequest.zipCode(),
                addressRequest.city(),
                addressRequest.province(),
                addressRequest.country()
        );
    }

    public static AddressResponse toAddressResponse(Address address) {
        return new AddressResponse(
                "The address is successfully added.",
                address.getStreetName(),
                address.getHouseNumber(),
                address.getHouseNumberAddition(),
                address.getZipCode(),
                address.getCity(),
                address.getProvince(),
                address.getCountry()
        );
    }
}
