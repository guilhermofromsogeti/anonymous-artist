package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.dto.CartItemDto;
import com.sogeti.java.anonymous_artist.request.CartItemEditRequest;
import com.sogeti.java.anonymous_artist.request.CartItemRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItemMapper {
    public static CartItemDto fromCartItemRequestToDto(CartItemRequest itemRequest) {
        return CartItemDto.builder()
                .id(itemRequest.id())
                .quantity(itemRequest.quantity())
                .build();
    }

    public static CartItemDto fromCartItemEditRequestToDto(CartItemEditRequest itemEditRequestRequest) {
        return CartItemDto.builder()
                .id(itemEditRequestRequest.id())
                .quantity(itemEditRequestRequest.quantity())
                .build();
    }
}
