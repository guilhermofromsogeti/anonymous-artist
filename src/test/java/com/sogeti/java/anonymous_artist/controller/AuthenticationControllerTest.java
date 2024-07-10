package com.sogeti.java.anonymous_artist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogeti.java.anonymous_artist.factory.AccountFactory;
import com.sogeti.java.anonymous_artist.request.AccountRequest;
import com.sogeti.java.anonymous_artist.service.AuthenticationService;
import com.sogeti.java.anonymous_artist.service.CustomUserDetailsService;
import com.sogeti.java.anonymous_artist.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AuthenticationController.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CustomUserDetailsService userDetailsService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void givenValidRequest_whenAuthenticatingAUser_thenUserIsAuthenticatedAndAuthenticationTokenIsReturned() throws Exception {
        // Given
        String email = "test@example.com";
        String password = "Passw0rd!";
        String firstName = "John";
        String jwtToken = "Generated Token";
        String expectedResponseBody = "Hi there, " + firstName + "!\njwt: " + jwtToken;
        AccountRequest accountRequest = AccountFactory.aRequest()
                .email(email)
                .password(password)
                .build();

        // When
        when(authenticationService.generateJwt(email)).thenReturn(jwtToken);
        when(authenticationService.getFirstNameOfUserByEmail(email)).thenReturn(firstName);

        // Then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/anonymous-artist/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(), HttpStatus.OK.value());
        assertEquals(mvcResult.getResponse().getContentAsString(), expectedResponseBody);

        verify(authenticationService).authenticateUser(accountRequest);
        verify(authenticationService).generateJwt(email);
        verify(authenticationService).getFirstNameOfUserByEmail(email);
        verifyNoMoreInteractions(authenticationService);
    }

    @Test
    void givenInValidRequest_whenAuthenticatingAUser_thenUserIsAuthenticatedAndAuthenticationTokenIsReturned() throws Exception {
        // Given
        String email = "test@example.com";
        String password = "Passw0rd!";
        AccountRequest invalidAccountRequest = AccountFactory.aRequest()
                .email(email)
                .password(password)
                .build();
        String exceptionMessage = "Incorrect email address or password, please enter a valid email or password";

        // When
        doThrow(new BadCredentialsException(exceptionMessage)).when(authenticationService).authenticateUser(any());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/anonymous-artist/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAccountRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
