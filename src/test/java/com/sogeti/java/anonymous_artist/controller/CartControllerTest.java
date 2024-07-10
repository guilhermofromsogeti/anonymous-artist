package com.sogeti.java.anonymous_artist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.dto.CartItemDto;
import com.sogeti.java.anonymous_artist.factory.CartFactory;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.request.CartItemEditRequest;
import com.sogeti.java.anonymous_artist.request.CartItemRequest;
import com.sogeti.java.anonymous_artist.response.CartResponse;
import com.sogeti.java.anonymous_artist.service.CartService;
import com.sogeti.java.anonymous_artist.service.CustomUserDetailsService;
import com.sogeti.java.anonymous_artist.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @MockBean
    CustomUserDetailsService userDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private CartService cartService;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void givenValidRequest_whenAddingACartItemRequestToCart_thenCreatedStatusAndResponseIsReturned() throws Exception {
        // Given
        CartItemRequest cartItemRequest = CartFactory.aCartItemRequest().build();
        CartItemDto expectedDto = CartFactory.fromCartItemRequestToDto(cartItemRequest);

        // When
        doNothing().when(cartService).addCartItemToCart(any(CartItemDto.class));
        MvcResult mvcResult = mockMvc.perform(post("/anonymous-artist/api/cart/add/")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Product with id " + expectedDto.id() + " is successfully added to the cart"))
                .andReturn();

        // Then
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        verify(cartService).addCartItemToCart(expectedDto);
        verifyNoMoreInteractions(cartService);
    }



    @Test
    void givenValidRequest_andQuantityZero_whenEditingASingleCartItem_thenRemoveCartItem() throws Exception {
        // Given
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        int quantity = 0;
        CartItemDto cartItemDto = CartFactory.aCartItemDto()
                .id(id)
                .quantity(quantity).build();
        Cart cart = CartFactory.aCart().build();
        String expectedResponse = "The cart item is removed";

        // When
        when(cartService.updateQuantityCartItem(cartItemDto)).thenReturn(expectedResponse);
        when(cartService.getCurrentUserCart()).thenReturn(cart);

        // Then
        String result = cartService.updateQuantityCartItem(cartItemDto);
        assertEquals(expectedResponse, result);
    }

    @Test
    void givenValidRequest_andQuantityNotZero_whenEditingASingleCartItem_thenSuccessMessageIsPassed() throws Exception {
        // Given
        String id = "978-4-3791-0133-9";
        int quantity = 4;
        CartItemEditRequest request = CartFactory.aCartItemEditRequest()
                .id(UUID.fromString(id))
                .quantity(quantity)
                .build();
        String expectedResponse = "The quantity of the cart item with id " + id + " is successfully updated to: " + quantity;

        // When - Then
        when(cartService.updateQuantityCartItem(any(CartItemDto.class))).thenReturn(expectedResponse);

        mockMvc.perform(put("/anonymous-artist/api/cart/edit/")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse))
                .andDo(print());
    }

    @Test
    void givenInvalidRequest_whenEditingASingleCartItem_thenBadRequestStatusIsReturned() throws Exception {
        // Given
        CartItemRequest cartItemRequest = CartFactory.aCartItemRequest().build();

        // When
        when(cartService.updateQuantityCartItem(any(CartItemDto.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        MvcResult mvcResult = mockMvc.perform(put("/anonymous-artist/api/cart/edit/")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        // Then
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void givenCurrentUserCartDto_whenGetAllCartItems_thenReturnCartResponse() throws Exception {
        // Given
        CartDto cartDto = CartFactory.createMockCartDto();
        CartResponse cartResponse = CartFactory.createCartResponse();

        // When & Then
        when(cartService.getOverviewCurrentCart()).thenReturn(cartDto);

        mockMvc.perform(get("/anonymous-artist/api/cart/")
                        .accept(APPLICATION_JSON))
                .andDo(print()) // This prints the request and response for debugging
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }
}
