package com.sogeti.java.anonymous_artist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogeti.java.anonymous_artist.factory.AccountFactory;
import com.sogeti.java.anonymous_artist.factory.AddressFactory;
import com.sogeti.java.anonymous_artist.factory.RegistrationFactory;
import com.sogeti.java.anonymous_artist.factory.UserFactory;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.request.AddressRequest;
import com.sogeti.java.anonymous_artist.request.RegistrationRequest;
import com.sogeti.java.anonymous_artist.request.UserRequest;
import com.sogeti.java.anonymous_artist.response.RegistrationResponse;
import com.sogeti.java.anonymous_artist.service.CustomUserDetailsService;
import com.sogeti.java.anonymous_artist.service.RegistrationService;
import com.sogeti.java.anonymous_artist.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
class RegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CustomUserDetailsService userDetailsService;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private RegistrationService registrationService;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void givenValidRequest_whenRegisterNewUser_thenRegistrationResponseIsReturned5() throws Exception {
        // Given
        RegistrationRequest validRegistrationRequest = RegistrationFactory.aRegistrationRequest();
        Account newAccount = AccountFactory.fromAccountRequestToAccount(validRegistrationRequest.getAccountRequest());
        RegistrationResponse expectedResponse = RegistrationFactory.fromAccount(newAccount);

        // When
        when(registrationService.registerNewUserWithAccountAndRole(any(User.class), any(Account.class), any(Address.class))).thenReturn(newAccount);

        // Then
        MvcResult mvcResult = mockMvc.perform(post("/anonymous-artist/api/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistrationRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/anonymous-artist/api/user/" + newAccount.getEmail()))
                .andReturn();

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertEquals("You have been successfully registered", expectedResponse.message());
        verify(registrationService).registerNewUserWithAccountAndRole(any(User.class), any(Account.class), any(Address.class));
        verifyNoMoreInteractions(registrationService);
    }

    @Test
    void givenInvalidRequest_whenRegisterNewUser_thenBadRequestIsPassed() throws Exception {
        // Given
        RegistrationRequest validRegistrationRequest = RegistrationFactory.anInvalidRegistrationRequestNoFirstName();

        // When
        when(registrationService.registerNewUserWithAccountAndRole(any(User.class), any(Account.class), any(Address.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // Then
        mockMvc.perform(post("/anonymous-artist/api/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegistrationRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidRequest_whenAccountRequestIsMissing_thenIncompleteRequestExceptionIsPassed() throws Exception {
        // Given
        UserRequest expectedUserRequest = UserFactory.aUserRequest().build();
        AddressRequest expectedAddressRequest = AddressFactory.anAddressRequest().build();


        // When
        when(registrationService.registerNewUserWithAccountAndRole(any(User.class), any(Account.class), any(Address.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // Then
        MvcResult mvcResult = mockMvc.perform(post("/anonymous-artist/api/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedUserRequest))
                        .content(objectMapper.writeValueAsString(expectedAddressRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

    }
}
