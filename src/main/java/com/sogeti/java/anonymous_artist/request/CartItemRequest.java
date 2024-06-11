package com.sogeti.java.anonymous_artist.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CartItemRequest(
        UUID id,
        @Min(value = 1, message = "Quantity should be 1 or higher")
        @Positive
        int quantity
) {
}
