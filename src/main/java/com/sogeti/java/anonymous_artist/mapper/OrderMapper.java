package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.dto.OrderDto;
import com.sogeti.java.anonymous_artist.dto.OrderHistoryDto;
import com.sogeti.java.anonymous_artist.dto.OrderItemDto;
import com.sogeti.java.anonymous_artist.model.CartItem;
import com.sogeti.java.anonymous_artist.model.Order;
import com.sogeti.java.anonymous_artist.model.OrderItem;
import com.sogeti.java.anonymous_artist.request.OrderRequest;
import com.sogeti.java.anonymous_artist.response.OrderHistoryResponse;
import com.sogeti.java.anonymous_artist.response.OrderResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

    public static OrderResponse fromOrderToOrderResponse(Order order) {
        return new OrderResponse(
                "Thank you for your order. Your order is being processed.",
                order.getOrderId()
        );
    }

    public static Order fromOrderRequest(OrderRequest orderRequest) {
        return new Order(
                orderRequest.orderId(),
                orderRequest.fullName(),
                orderRequest.shippingAddress(),
                orderRequest.email(),
                orderRequest.phoneNumber(),
                orderRequest.dateOfOrder()
        );
    }

    public static OrderDto fromOrderRequestToDto(OrderRequest orderRequest) {
        return new OrderDto(
                orderRequest.orderId(),
                orderRequest.fullName(),
                orderRequest.shippingAddress(),
                orderRequest.email(),
                orderRequest.phoneNumber(),
                orderRequest.dateOfOrder()
        );
    }

    public static OrderResponse fromOrderDtoToOrderResponse(OrderDto orderDto) {
        return new OrderResponse(
                "Thank you for your order. Your order is being processed.",
                orderDto.orderId()
        );
    }

    public static Order fromOrderDtoAndCartDtoToOrder(OrderDto orderDto, CartDto cartDto) {
        return new Order(
                orderDto.fullName(),
                orderDto.shippingAddress(),
                orderDto.email(),
                orderDto.phoneNumber(),
                orderDto.dateOfOrder(),
                cartDto.totalPrice()
        );
    }

    public static OrderItem fromCartItemToOrderItem(CartItem cartItem) {
        return new OrderItem(
                cartItem.getProductTitle(),
                cartItem.getQuantity(),
                cartItem.getProductPrice(),
                cartItem.getSubTotalPrice()
        );
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

    public static Order fromOrderDtoToOrder(OrderDto orderDto) {
        return new Order(
                orderDto.orderId(),
                orderDto.fullName(),
                orderDto.shippingAddress(),
                orderDto.email(),
                orderDto.phoneNumber(),
                orderDto.dateOfOrder()
        );
    }

    public static OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getOrderItemId(),
                orderItem.getQuantity(),
                orderItem.getProductTitle(),
                orderItem.getUnitPrice(),
                orderItem.getSubTotalPrice()
        );
    }

    public static OrderHistoryDto fromOrderToOrderHistoryDto(Order order) {
        return new OrderHistoryDto(
                order.getOrderId(),
                order.getDateOfOrder(),
                order.getTotalPrice(),
                order.getOrderItems().stream().map(OrderMapper::fromOrderItemToOrderItemDto).toList());
    }

    public static OrderHistoryResponse fromOrderHistoryDtoToOrderHistoryResponse(OrderHistoryDto orderHistoryDto) {
        return new OrderHistoryResponse(
                orderHistoryDto.orderID(),
                orderHistoryDto.orderDate(),
                orderHistoryDto.totalOrderPrice(),
                orderHistoryDto.orderItemDtoList());
    }
}
