package com.sogeti.java.anonymous_artist.factory;

import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.request.RegistrationRequest;
import com.sogeti.java.anonymous_artist.response.RegistrationResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationFactory {


    public static RegistrationResponse fromAccount(Account account) {
        return new RegistrationResponse(
                "You have been successfully registered",
                account.getEmail()
        );
    }

    public static RegistrationRequest aRegistrationRequest() {
        return new RegistrationRequest(
                UserFactory.aUserRequest().build(),
                AccountFactory.aRequest().build(),
                AddressFactory.anAddressRequest().build());
    }

    public static RegistrationRequest anInvalidRegistrationRequestNoFirstName() {
        return new RegistrationRequest(
                UserFactory.aUserRequest().
                        infix("").
                        lastName("last").
                        dateOfBirth(LocalDate.of(1999, 1, 1)).
                        phoneNumber("0612345678").build(),
                AccountFactory.aRequest().build(),
                AddressFactory.anAddressRequest().build());
    }


}
