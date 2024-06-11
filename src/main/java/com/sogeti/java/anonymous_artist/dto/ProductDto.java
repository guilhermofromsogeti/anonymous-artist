package com.sogeti.java.anonymous_artist.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto(
        UUID id,
        String title,
        String smallSummary,
        String description,
        BigDecimal price,
        Long amountInStock
) {
}
