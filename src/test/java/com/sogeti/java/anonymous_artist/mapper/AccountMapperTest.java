package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.factory.AccountFactory;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.request.AccountRequest;
import com.sogeti.java.anonymous_artist.response.AccountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountMapperTest {

    @MockBean
    AccountMapper accountMapper;

    @Test
    void givenAccountRequest_whenMappedToAccount_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        AccountRequest accountRequest = AccountFactory.aRequest().build();

        // When
        Account account = AccountMapper.fromAccountRequest(accountRequest);

        // Then
        assertEquals(account.getEmail(), accountRequest.email());
        assertEquals(account.getPassword(), accountRequest.password());


    }

    @Test
    void givenAccount_whenMappedToAccountResponse_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        Account account = AccountFactory.anAccount().build();

        // When
        AccountResponse accountResponse = AccountMapper.toAccountResponse(account);

        // Then
        assertEquals(account.getEmail(), accountResponse.email());
    }
}
