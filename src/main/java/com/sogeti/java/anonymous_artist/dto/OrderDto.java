package com.sogeti.java.anonymous_artist.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderDto(
        UUID orderId,
        String fullName,
        String shippingAddress,
        String email,
        String phoneNumber,
        LocalDateTime dateOfOrder
) {
}
