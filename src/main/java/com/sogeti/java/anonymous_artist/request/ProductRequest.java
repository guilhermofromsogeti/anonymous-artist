package com.sogeti.java.anonymous_artist.request;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(
        UUID id,
        String title,
        String smallSummary,
        String description,
        BigDecimal price,
        Long amountInStock
) {
}
