package com.sogeti.java.anonymous_artist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sogeti.java.anonymous_artist.dto.OrderDto;
import com.sogeti.java.anonymous_artist.dto.OrderHistoryDto;
import com.sogeti.java.anonymous_artist.factory.OrderFactory;
import com.sogeti.java.anonymous_artist.model.Order;
import com.sogeti.java.anonymous_artist.request.OrderRequest;
import com.sogeti.java.anonymous_artist.service.CustomUserDetailsService;
import com.sogeti.java.anonymous_artist.service.OrderService;
import com.sogeti.java.anonymous_artist.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    OrderService orderService;
    @MockBean
    CustomUserDetailsService userDetailsService;
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void givenAValidRequest_whenCreatingAnOrder_thenAnOrderResponseShouldBeReturned() throws Exception {
        // Given
        UUID validCartId = UUID.randomUUID();
        OrderRequest orderRequest = OrderFactory.anOrderRequest().orderId(validCartId).build();
        Order expectedOrder = OrderFactory.fromOrderRequestToOrder(orderRequest);
        OrderDto expectedOrderDto = OrderFactory.fromOrderRequestToOrderDto(expectedOrder);

        // When
        when(orderService.createAnOrderForAUser(expectedOrderDto)).thenReturn(expectedOrderDto);

        // Then
        mockMvc.perform(post("/anonymous-artist/api/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(result -> OrderFactory.fromOrderToResponse(expectedOrder));


        verify(orderService).createAnOrderForAUser(expectedOrderDto);
        verifyNoMoreInteractions(orderService);
    }


    @Test
    void givenFirstNameIsEmpty_whenCreatingANewOrder_thenStatus400BadRequestIsReturnedWithValidation() throws Exception {
        // Given
        OrderRequest orderRequest = OrderFactory.orderMissingFirstNameRequest().build();
        Order expectedOrder = OrderFactory.fromOrderRequestToOrder(orderRequest);
        OrderDto expectedOrderDto = OrderFactory.fromOrderRequestToOrderDto(expectedOrder);

        // When
        when(orderService.createAnOrderForAUser(expectedOrderDto)).thenReturn(expectedOrderDto);

        // Then
        mockMvc.perform(post("/anonymous-artist/api/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void givenEmptyOrderHistory_whenGetOrderHistory_thenReturnMessage() throws Exception {
        // Given
        when(orderService.getOrderHistory()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/anonymous-artist/api/order/history/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("It seems that you don't have a purchase history."));
    }

    @Test
    void givenValidEmail_whenOrderHistoryIsNotEmpty_thenReturnOrderHistoryList() throws Exception {
        // Given
        String email = "user@example.com";
        List<OrderHistoryDto> orderHistoryDtos = List.of(OrderFactory.anOrderHistoryDto()); // Mock some DTOs
        when(orderService.getOrderHistory()).thenReturn(orderHistoryDtos);

        // When & Then
        mockMvc.perform(get("/anonymous-artist/api/order/history/", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }
}
