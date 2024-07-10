package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;


    @Test
    void givenValidUserLoginEmailAndPassword_whenCorrectAccountIsGiven_thenEmailPasswordAndAuthoritiesIsReturned() {
        // Given
        Account account = new Account("testsjedoejeding@gmail.com", "testjeHeeftWW!1");

        // When
        when(accountRepository.findById(account.getEmail())).thenReturn(Optional.of(account));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(account.getEmail());

        // Then
        assertNotNull(userDetails);
        assertEquals(account.getEmail(), userDetails.getUsername());
        assertEquals(account.getPassword(), userDetails.getPassword());
    }
}
