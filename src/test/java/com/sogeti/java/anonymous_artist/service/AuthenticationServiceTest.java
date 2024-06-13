package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.repository.AccountRepository;
import com.sogeti.java.anonymous_artist.request.AccountRequest;
import com.sogeti.java.anonymous_artist.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidEmailAddress_whenGenerateJwt_thenReturnJwtAsString() {
        // Given
        String email = "test@example.com";
        UserDetails userDetails = mock(UserDetails.class);
        String jwtToken = "Generated token";

        // When
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn(jwtToken);
        String generatedJwt = authenticationService.generateJwt(email);

        // Then
        assertEquals(generatedJwt, jwtToken);
    }

    @Test
    void givenValidLoginCredentials_whenAuthenticatingCredentials_thenNewUsernamePasswordAuthenticationTokenIsCreated() {
        // Given
        String email = "test@example.com";
        String password = "password";
        AccountRequest accountRequest = new AccountRequest(email, password);

        // When
        authenticationService.authenticateUser(accountRequest);

        // Then
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void givenInvalidLoginCredentials_whenAuthenticatingCredentials_thenBadCredentialsExceptionIsThrown() {
        // Given
        String email = "test@example.com";
        String password = "password";
        AccountRequest accountRequest = new AccountRequest(email, password);

        // When
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(anyString()));

        // Then
        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateUser(accountRequest));
    }
}
