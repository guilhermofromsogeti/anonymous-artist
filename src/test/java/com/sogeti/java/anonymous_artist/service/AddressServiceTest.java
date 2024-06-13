package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.factory.AddressFactory;
import com.sogeti.java.anonymous_artist.factory.UserFactory;
import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.repository.AddressRepository;
import com.sogeti.java.anonymous_artist.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    void givenNoConflicts_whenCreatingAnAddress_thenCreateAddressIsCreated() {
        // Given
        Address address = AddressFactory.anAddress().build();

        // When
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.createAddress(address);

        // Then
        assertEquals(result, address);
        verify(addressRepository).save(any(Address.class));
        verifyNoMoreInteractions(addressRepository);
    }

    @Test
    void testAddAddressToExistingUser() {
        // Given
        User mockUser = UserFactory.aUser().build();
        Address inputAddress = AddressFactory.anAddress().build();
        when(userRepository.findById(mockUser.getMembershipId())).thenReturn(Optional.of(mockUser));

        // When
        Address savedAddress = addressService.addAddressToExistingUser(mockUser.getMembershipId(), inputAddress);

        // Then
        assertEquals(savedAddress, inputAddress);
        assertNotNull(savedAddress);
        assertEquals(inputAddress.getStreetName(), savedAddress.getStreetName());
        assertEquals(inputAddress.getHouseNumber(), savedAddress.getHouseNumber());
        assertEquals(inputAddress.getZipCode(), savedAddress.getZipCode());


        verify(userRepository, times(1)).findById(mockUser.getMembershipId());
        verify(addressRepository, times(1)).save(argThat(address ->
                address.getUser() != null &&
                        address.getUser().equals(mockUser) &&
                        address.getStreetName().equals(inputAddress.getStreetName()) &&
                        address.getHouseNumber().equals(inputAddress.getHouseNumber()) &&
                        address.getZipCode().equals(inputAddress.getZipCode()) &&
                        address.getCity().equals(inputAddress.getCity()) &&
                        address.getProvince().equals(inputAddress.getProvince()) &&
                        address.getCountry().equals(inputAddress.getCountry())));
    }
}



