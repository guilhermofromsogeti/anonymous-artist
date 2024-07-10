package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.factory.AddressFactory;
import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.request.AddressRequest;
import com.sogeti.java.anonymous_artist.response.AddressResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressMapperTest {

    @MockBean
    AddressMapper addressMapper;

    @Test
    void givenAddressRequest_whenMappedToAddress_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        AddressRequest addressRequest = AddressFactory.anAddressRequest().build();

        // When
        Address address = AddressMapper.fromAddressRequest(addressRequest);

        // Then
        assertEquals(address.getStreetName(), addressRequest.streetName());
        assertEquals(address.getHouseNumber(), addressRequest.houseNumber());
        assertEquals(address.getHouseNumberAddition(), addressRequest.houseNumberAddition());
        assertEquals(address.getZipCode(), addressRequest.zipCode());
        assertEquals(address.getCity(), addressRequest.city());
        assertEquals(address.getProvince(), addressRequest.province());
        assertEquals(address.getCountry(), addressRequest.country());

    }

    @Test
    void givenAddress_whenMappedToAddressResponse_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        Address address = AddressFactory.anAddress().build();

        // When
        AddressResponse addressResponse = AddressMapper.toAddressResponse(address);

        // Then
        assertEquals("The address is successfully added.", addressResponse.message());
        assertEquals(address.getStreetName(), addressResponse.streetName());
        assertEquals(address.getHouseNumber(), addressResponse.houseNumber());
        assertEquals(address.getHouseNumberAddition(), addressResponse.houseNumberAddition());
        assertEquals(address.getZipCode(), addressResponse.zipCode());
        assertEquals(address.getCity(), addressResponse.city());
        assertEquals(address.getProvince(), addressResponse.province());
        assertEquals(address.getCountry(), addressResponse.country());

    }
}
