package com.sogeti.java.anonymous_artist.controller;

import com.sogeti.java.anonymous_artist.mapper.AccountMapper;
import com.sogeti.java.anonymous_artist.mapper.AddressMapper;
import com.sogeti.java.anonymous_artist.mapper.RegistrationMapper;
import com.sogeti.java.anonymous_artist.mapper.UserMapper;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.request.RegistrationRequest;
import com.sogeti.java.anonymous_artist.response.RegistrationResponse;
import com.sogeti.java.anonymous_artist.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/anonymous-artist/api/user/")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping()
    public ResponseEntity<RegistrationResponse> registerNewUserWithAccountAndRole(@RequestBody @Valid
                                                                                  RegistrationRequest registrationRequest) {

        User newUser = UserMapper.fromUserRequest(registrationRequest.getUserRequest());
        Account newAccount = AccountMapper.fromAccountRequest(registrationRequest.getAccountRequest());
        Address newAddress = AddressMapper.fromAddressRequest(registrationRequest.getAddressRequest());

        registrationService.registerNewUserWithAccountAndRole(newUser, newAccount, newAddress);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{email}")
                .buildAndExpand(newAccount.getEmail()).toUri();

        RegistrationResponse registrationResponse = RegistrationMapper.toRegistrationResponse(newAccount);

        return ResponseEntity.created(location).body(registrationResponse);
    }
}
