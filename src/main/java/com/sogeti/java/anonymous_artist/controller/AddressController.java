package com.sogeti.java.anonymous_artist.controller;

import com.sogeti.java.anonymous_artist.mapper.AddressMapper;
import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.request.AddressRequest;
import com.sogeti.java.anonymous_artist.response.AddressResponse;
import com.sogeti.java.anonymous_artist.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/anonymous-artist/api/address/")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("{userId}")
    public ResponseEntity<AddressResponse> addAddressToUser(@PathVariable UUID userId,
                                                            @RequestBody @Valid AddressRequest addressRequest) {

        Address newAddress = AddressMapper.fromAddressRequest(addressRequest);
        addressService.addAddressToExistingUser(userId, newAddress);
        AddressResponse newAddressResponse = AddressMapper.toAddressResponse(newAddress);

        return ResponseEntity.status(HttpStatus.CREATED).body(newAddressResponse);
    }
}
