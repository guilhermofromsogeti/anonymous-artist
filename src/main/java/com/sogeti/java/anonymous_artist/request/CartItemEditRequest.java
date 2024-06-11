package com.sogeti.java.anonymous_artist.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CartItemEditRequest(
        UUID id,
        @Min(value = 0, message = "Quantity can not be lower then 0.")
        int quantity
) {
}
