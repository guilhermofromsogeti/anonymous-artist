package com.sogeti.java.anonymous_artist.factory;

import com.github.javafaker.Faker;

import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.request.AddressRequest;
import com.sogeti.java.anonymous_artist.response.AddressResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressFactory {

    private final static Faker faker = new Faker();

    public static Address.AddressBuilder anAddress() {
        return Address.builder()
                .addressId(UUID.randomUUID())
                .streetName(faker.address().streetName())
                .houseNumber(faker.address().streetAddressNumber())
                .houseNumberAddition(faker.toString())
                .zipCode(faker.address().zipCode())
                .city(faker.address().city())
                .province(faker.address().state())
                .country(faker.address().country());
    }

    public static AddressRequest.AddressRequestBuilder anAddressRequest() {
        return AddressRequest.builder()
                .streetName(faker.address().streetName())
                .houseNumber(faker.address().streetAddressNumber())
                .houseNumberAddition(faker.toString())
                .zipCode(faker.address().zipCode())
                .city(faker.address().city())
                .province(faker.address().state())
                .country(faker.address().country());
    }

    public static Address fromAddressRequestToAddress(AddressRequest addressRequest) {
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

    public static AddressResponse fromAddressToAddressResponse(Address address) {
        return new AddressResponse(
                "The address is successfully added.",
                address.getStreetName(),
                address.getHouseNumber(),
                address.getHouseNumberAddition(),
                address.getZipCode(),
                address.getCity(),
                address.getProvince(),
                address.getCountry());
    }
}
