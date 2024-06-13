package com.sogeti.java.anonymous_artist.factory;

import com.github.javafaker.Faker;
import com.sogeti.java.anonymous_artist.dto.OrderDto;
import com.sogeti.java.anonymous_artist.dto.OrderHistoryDto;
import com.sogeti.java.anonymous_artist.model.Order;
import com.sogeti.java.anonymous_artist.model.OrderItem;
import com.sogeti.java.anonymous_artist.request.OrderRequest;
import com.sogeti.java.anonymous_artist.response.OrderHistoryResponse;
import com.sogeti.java.anonymous_artist.response.OrderResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderFactory {

    private final static Faker faker = new Faker();

    public static Order.OrderBuilder anOrder() {
        return Order.builder()
                .orderId(UUID.randomUUID())
                .fullName(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .shippingAddress(faker.address().fullAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .dateOfOrder(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
    }

    public static OrderRequest.OrderRequestBuilder aRandomOrderRequest() {
        return OrderRequest.builder()
                .fullName(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .shippingAddress(faker.address().fullAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .dateOfOrder(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
    }

    public static OrderRequest.OrderRequestBuilder anOrderRequest() {
        return OrderRequest.builder()
                .orderId(UUID.randomUUID())
                .fullName("Test van de Teeeeeessster")
                .email("test@test.nl")
                .shippingAddress("Somestreet 9, 1111AA, Tilburg, Netherlands")
                .phoneNumber("1L")
                .dateOfOrder(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
    }

    public static OrderRequest.OrderRequestBuilder orderMissingFirstNameRequest() {
        return OrderRequest.builder()

                .fullName("")
                .email("test@test.nl")
                .shippingAddress("Somestreet 9, 1111AA, Tilburg, Netherlands")
                .phoneNumber("1L")
                .dateOfOrder(LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS));
    }

    public static Order fromOrderRequestToOrder(OrderRequest orderRequest) {
        return new Order(
                orderRequest.orderId(),
                orderRequest.fullName(),
                orderRequest.email(),
                orderRequest.shippingAddress(),
                orderRequest.phoneNumber(),
                orderRequest.dateOfOrder()
        );
    }

    public static OrderResponse fromOrderToResponse(Order order) {
        return new OrderResponse(
                "Thank you for your order. Your order is being processed.",
                order.getOrderId()
        );
    }

    public static OrderDto fromOrderRequestToOrderDto(Order expectedOrder) {
        return new OrderDto(
                expectedOrder.getOrderId(),
                expectedOrder.getFullName(),
                expectedOrder.getEmail(),
                expectedOrder.getShippingAddress(),
                expectedOrder.getPhoneNumber(),
                expectedOrder.getDateOfOrder()
        );
    }

    public static OrderDto createMockOrderDto() {
        return OrderDto.builder()
                .orderId(UUID.randomUUID())
                .build();
    }

    public static OrderDto fromOrderToOrderDto(Order order) {
        return new OrderDto(
                order.getOrderId(),
                order.getFullName(),
                order.getShippingAddress(),
                order.getEmail(),
                order.getPhoneNumber(),
                order.getDateOfOrder()
        );
    }

    public static OrderItem anOrderItem() {
        return OrderItem.builder()
                .orderItemId(UUID.randomUUID())
                .productTitle(faker.lorem().sentence())
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(17.55))
                .subTotalPrice((BigDecimal.valueOf(2)))
                .build();
    }

    public static OrderHistoryDto anOrderHistoryDto() {
        return OrderHistoryDto.builder().build();
    }

    public static OrderHistoryResponse anOrderHistoryResponse() {
        return OrderHistoryResponse.builder().build();
    }
}