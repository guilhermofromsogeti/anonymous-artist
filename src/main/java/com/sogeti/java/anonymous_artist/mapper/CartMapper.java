package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.response.CartResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartMapper {

    public static CartResponse fromCartDtoToCartResponse(CartDto cartDto) {
        return new CartResponse(
                cartDto.totalPrice(),
                cartDto.cartItemList());
    }

    public static CartDto fromCartToCartDto(Cart cart) {
        return new CartDto(
                cart.getTotalPrice(),
                cart.getCartItemList());
    }

    public static Cart fromCartDtoToCart(CartDto cartDto) {
        return new Cart(
                cartDto.totalPrice(),
                cartDto.cartItemList());
    }
}
