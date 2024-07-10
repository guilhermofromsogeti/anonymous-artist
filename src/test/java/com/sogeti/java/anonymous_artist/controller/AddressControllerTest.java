package com.sogeti.java.anonymous_artist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogeti.java.anonymous_artist.factory.AddressFactory;
import com.sogeti.java.anonymous_artist.mapper.AddressMapper;
import com.sogeti.java.anonymous_artist.model.Address;
import com.sogeti.java.anonymous_artist.request.AddressRequest;
import com.sogeti.java.anonymous_artist.response.AddressResponse;
import com.sogeti.java.anonymous_artist.service.AddressService;
import com.sogeti.java.anonymous_artist.service.CustomUserDetailsService;
import com.sogeti.java.anonymous_artist.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
class AddressControllerTest {

    @MockBean
    CustomUserDetailsService userDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private AddressService addressService;

    @Test
    void givenAValidRequest_whenCreatingANewAddress_thenACreatedAddressShouldBeAddedToExistingUser() throws Exception {
        // Given
        UUID existingUserId = UUID.randomUUID();
        AddressRequest addressRequest = AddressFactory.anAddressRequest().build();

        Address newAddress = AddressMapper.fromAddressRequest(addressRequest);
        AddressResponse newAddressResponse = AddressFactory.fromAddressToAddressResponse(newAddress);

        when(addressService.addAddressToExistingUser(any(UUID.class), any(Address.class))).thenReturn(newAddress);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/anonymous-artist/api/address/{userId}", existingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertThat(newAddressResponse.message()).isEqualTo("The address is successfully added.");

        verify(addressService).addAddressToExistingUser(any(UUID.class), any(Address.class));
        verifyNoMoreInteractions(addressService);
    }
}
