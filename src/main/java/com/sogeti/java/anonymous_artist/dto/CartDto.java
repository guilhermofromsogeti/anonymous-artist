package com.sogeti.java.anonymous_artist.dto;

import com.sogeti.java.anonymous_artist.model.CartItem;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartDto(
        BigDecimal totalPrice,
        List<CartItem> cartItemList
) {
}
