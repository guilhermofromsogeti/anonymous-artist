package com.sogeti.java.anonymous_artist.factory;

import com.github.javafaker.Faker;
import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.dto.CartItemDto;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.model.CartItem;
import com.sogeti.java.anonymous_artist.request.CartItemEditRequest;
import com.sogeti.java.anonymous_artist.request.CartItemRequest;
import com.sogeti.java.anonymous_artist.response.CartResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartFactory {
    private final static Faker faker = new Faker();

    public static CartItemRequest.CartItemRequestBuilder aCartItemRequest() {
        return CartItemRequest.builder()
                .id(UUID.randomUUID())
                .quantity(faker.number().numberBetween(1, 5));
    }

    public static CartItemEditRequest.CartItemEditRequestBuilder aCartItemEditRequest() {
        return CartItemEditRequest.builder()
                .id(UUID.randomUUID())
                .quantity(faker.number().numberBetween(1, 5));
    }

    public static CartItemDto.CartItemDtoBuilder aCartItemDto() {
        return CartItemDto.builder()
                .id(UUID.randomUUID())
                .quantity(faker.number().numberBetween(1, 5));
    }

    public static CartItemDto.CartItemDtoBuilder fullCartItemDto() {
        return CartItemDto.builder()
                .id(UUID.randomUUID())
                .productPrice(BigDecimal.valueOf(50.00))
                .quantity(2)
                .productTitle(faker.book().title())
                .subTotalPrice(BigDecimal.valueOf(100));
    }

    public static CartItemDto fromCartItemRequestToDto(CartItemRequest itemRequest) {
        return CartItemDto.builder()
                .id(itemRequest.id())
                .quantity(itemRequest.quantity())
                .build();
    }

    public static Cart.CartBuilder aCart() {
        return Cart.builder()
                .cartId(UUID.randomUUID())
                .cartItemList(new ArrayList<>());
    }

    public static CartItem.CartItemBuilder aCartItem() {
        BigDecimal productPrice = BigDecimal.valueOf(17.65);
        int quantity = 1;

        return CartItem.builder()
                .cartItemId(UUID.randomUUID())
                .productTitle("Title of a product")
                .quantity(quantity)
                .productPrice(productPrice)
                .subTotalPrice(productPrice.multiply(BigDecimal.valueOf(quantity)));
    }

    public static CartDto createMockCartDto() {
        return CartDto.builder()
                .totalPrice(BigDecimal.valueOf(0))
                .cartItemList(Collections.emptyList())
                .build();
    }

    public static CartResponse createCartResponse() {
        return CartResponse.builder()
                .cartItemList(Collections.emptyList())
                .build();
    }

    public static CartDto fromCartToCartDto(Cart cart) {
        return new CartDto(
                cart.getTotalPrice(),
                cart.getCartItemList());
    }
}