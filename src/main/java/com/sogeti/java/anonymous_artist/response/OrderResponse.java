package com.sogeti.java.anonymous_artist.response;

import java.util.UUID;

public record OrderResponse(
        String message,
        UUID orderId) {
}
