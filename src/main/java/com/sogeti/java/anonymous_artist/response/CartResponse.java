package com.sogeti.java.anonymous_artist.response;

import com.sogeti.java.anonymous_artist.model.CartItem;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
@Builder
public record CartResponse(
        BigDecimal totalPrice,
        List<CartItem> cartItemList
) {
}
