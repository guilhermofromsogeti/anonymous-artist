package com.sogeti.java.anonymous_artist.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDto(
        UUID orderItemID,
        int quantity,
        String productTitle,
        BigDecimal productPrice,
        BigDecimal subTotalPrice
) {
}
