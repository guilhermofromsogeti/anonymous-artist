package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.response.RegistrationResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationMapper {

    public static RegistrationResponse toRegistrationResponse(Account account) {
        return new RegistrationResponse(
                "You have been successfully registered",
                account.getEmail()
        );
    }
}
