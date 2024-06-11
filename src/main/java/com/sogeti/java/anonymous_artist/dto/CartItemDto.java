package com.sogeti.java.anonymous_artist.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CartItemDto(
        UUID id,
        int quantity,
        String productTitle,
        BigDecimal productPrice,
        BigDecimal subTotalPrice
) {
}
