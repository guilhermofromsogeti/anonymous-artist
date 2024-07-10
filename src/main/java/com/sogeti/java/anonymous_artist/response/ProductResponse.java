package com.sogeti.java.anonymous_artist.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductResponse(
        UUID id,
        String title,
        String smallSummary,
        String description,
        BigDecimal price,
        Integer amountInStock
) {

}
