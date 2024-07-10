package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.dto.OrderDto;
import com.sogeti.java.anonymous_artist.dto.OrderHistoryDto;
import com.sogeti.java.anonymous_artist.factory.CartFactory;
import com.sogeti.java.anonymous_artist.factory.OrderFactory;
import com.sogeti.java.anonymous_artist.model.CartItem;
import com.sogeti.java.anonymous_artist.model.Order;
import com.sogeti.java.anonymous_artist.model.OrderItem;
import com.sogeti.java.anonymous_artist.request.OrderRequest;
import com.sogeti.java.anonymous_artist.response.OrderHistoryResponse;
import com.sogeti.java.anonymous_artist.response.OrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderMapperTest {

    @MockBean
    private OrderMapper orderMapper;

    @Test
    void givenOrder_whenMappedToOrderResponse_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        Order order = OrderFactory.anOrder().build();

        // When
        OrderResponse orderResponse = OrderMapper.fromOrderToOrderResponse(order);

        // Then
        assertEquals("Thank you for your order. Your order is being processed.", orderResponse.message());
        assertEquals(order.getOrderId(), orderResponse.orderId());
    }

    @Test
    void givenOrderRequest_whenMappedToOrder_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        OrderRequest orderRequest = OrderFactory.anOrderRequest().build();

        // When
        Order order = OrderMapper.fromOrderRequest(orderRequest);

        // Then
        assertEquals(orderRequest.fullName(), order.getFullName());
        assertEquals(orderRequest.email(), order.getEmail());
        assertEquals(orderRequest.shippingAddress(), order.getShippingAddress());
        assertEquals(orderRequest.phoneNumber(), order.getPhoneNumber());
        assertEquals(orderRequest.dateOfOrder(), order.getDateOfOrder());
    }

    @Test
    void givenOrderRequest_whenMappedToOrderDto_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        OrderRequest orderRequest = OrderFactory.anOrderRequest().build();

        // When
        OrderDto orderDto = OrderMapper.fromOrderRequestToDto(orderRequest);

        // Then
        assertEquals(orderRequest.orderId(), orderDto.orderId());
        assertEquals(orderRequest.fullName(), orderDto.fullName());
        assertEquals(orderRequest.shippingAddress(), orderDto.shippingAddress());
        assertEquals(orderRequest.email(), orderDto.email());
        assertEquals(orderRequest.phoneNumber(), orderDto.phoneNumber());
        assertEquals(orderRequest.dateOfOrder(), orderDto.dateOfOrder());
    }

    @Test
    void givenOrderDto_whenMappedToOrderResponse_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        OrderDto orderDto = OrderFactory.createMockOrderDto();

        // When
        OrderResponse orderResponse = OrderMapper.fromOrderDtoToOrderResponse(orderDto);

        // Then
        assertEquals("Thank you for your order. Your order is being processed.", orderResponse.message());
        assertEquals(orderDto.orderId(), orderResponse.orderId());
    }

    @Test
    void givenOrderDto_andCartDto_whenMappedToOrder_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        OrderDto orderDto = OrderFactory.createMockOrderDto();
        CartDto cartDto = CartFactory.createMockCartDto();

        // When
        Order order = OrderMapper.fromOrderDtoAndCartDtoToOrder(orderDto, cartDto);

        // Then
        assertEquals(orderDto.fullName(), order.getFullName());
        assertEquals(orderDto.shippingAddress(), order.getShippingAddress());
        assertEquals(orderDto.email(), order.getEmail());
        assertEquals(orderDto.phoneNumber(), order.getPhoneNumber());
        assertEquals(orderDto.dateOfOrder(), order.getDateOfOrder());
        assertEquals(cartDto.totalPrice(), order.getTotalPrice());
    }

    @Test
    void givenCartItem_whenMappedToOrderItem_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        CartItem cartItem = CartFactory.aCartItem().build();

        // When
        OrderItem orderItem = OrderMapper.fromCartItemToOrderItem(cartItem);

        // Then
        assertEquals(cartItem.getProductTitle(), orderItem.getProductTitle());
        assertEquals(cartItem.getQuantity(), orderItem.getQuantity());
        assertEquals(cartItem.getProductPrice(), orderItem.getUnitPrice());
        assertEquals(cartItem.getSubTotalPrice(), orderItem.getSubTotalPrice());
    }

    @Test
    void givenOrder_whenMappedToOrderDto_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        Order order = OrderFactory.anOrder().build();

        // When
        OrderDto orderDto = OrderMapper.fromOrderToOrderDto(order);

        // Then
        assertEquals(order.getOrderId(), orderDto.orderId());
        assertEquals(order.getFullName(), orderDto.fullName());
        assertEquals(order.getShippingAddress(), orderDto.shippingAddress());
        assertEquals(order.getEmail(), orderDto.email());
        assertEquals(order.getPhoneNumber(), orderDto.phoneNumber());
        assertEquals(order.getDateOfOrder(), orderDto.dateOfOrder());
    }

    @Test
    void givenOrderDto_whenMappedToOrder_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        OrderDto orderDto = OrderFactory.createMockOrderDto();

        // When
        Order order = OrderMapper.fromOrderDtoToOrder(orderDto);

        // Then
        assertEquals(orderDto.orderId(), order.getOrderId());
        assertEquals(orderDto.fullName(), order.getFullName());
        assertEquals(orderDto.shippingAddress(), order.getShippingAddress());
        assertEquals(orderDto.email(), order.getEmail());
        assertEquals(orderDto.phoneNumber(), order.getPhoneNumber());
        assertEquals(orderDto.dateOfOrder(), order.getDateOfOrder());
    }


    @Test
    void givenOrderHistoryDto_whenMappedToOrderHistoryResponse_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        OrderHistoryDto orderHistoryDto = OrderFactory.anOrderHistoryDto();

        // When
        OrderHistoryResponse orderHistoryResponse = OrderMapper.fromOrderHistoryDtoToOrderHistoryResponse(orderHistoryDto);

        // Then
        assertEquals(orderHistoryDto.orderID(), orderHistoryResponse.orderID());
        assertEquals(orderHistoryDto.orderDate(), orderHistoryResponse.orderDate());
        assertEquals(orderHistoryDto.totalOrderPrice(), orderHistoryResponse.totalOrderPrice());
        assertEquals(orderHistoryDto.orderItemDtoList(), orderHistoryResponse.orderItemDtoList());
    }


}