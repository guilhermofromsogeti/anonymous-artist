package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.factory.AccountFactory;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.response.RegistrationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegistrationMapperTest {

    @MockBean
    RegistrationMapper registrationMapper;

    @Test
    void givenAccount_whenMappedToRegistrationResponse_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        Account account = AccountFactory.anAccount().build();

        // When
        RegistrationResponse registrationResponse = RegistrationMapper.toRegistrationResponse(account);

        // Then
        assertEquals(account.getEmail(), registrationResponse.email());
        assertEquals("You have been successfully registered", registrationResponse.message());


    }
}
