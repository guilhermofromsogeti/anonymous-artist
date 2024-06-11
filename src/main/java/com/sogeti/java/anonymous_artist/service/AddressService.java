package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.repository.AddressRepository;
import com.sogeti.java.anonymous_artist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public Address createAddress(Address newAddress) {
        Address createNewAddress = new Address(
                newAddress.getStreetName(),
                newAddress.getHouseNumber(),
                newAddress.getHouseNumberAddition(),
                newAddress.getZipCode(),
                newAddress.getCity(),
                newAddress.getProvince(),
                newAddress.getCountry());

        return addressRepository.save(createNewAddress);
    }

    public Address addAddressToExistingUser(UUID userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException(userId.toString().replace("-", "")));
        address.setUser(user);
        addressRepository.save(address);
        return address;
    }
}
