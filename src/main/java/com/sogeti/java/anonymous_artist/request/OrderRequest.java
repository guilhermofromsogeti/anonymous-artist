package com.sogeti.java.anonymous_artist.request;

import com.sogeti.java.anonymous_artist.model.CartItem;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderRequest(
        UUID orderId,
        @NotBlank(message = "Please fill in your first name.")
        String fullName,

        @NotBlank(message = "Please fill in the shipping address.")
        String shippingAddress,

        @Email(message = "Please fill in a valid email address.")
        @NotBlank(message = "Please fill in your email address.")
        String email,

        @NotBlank(message = "Please fill in your phone number.")
        String phoneNumber,
        LocalDateTime dateOfOrder,

        List<CartItem> cartItemList
) {
    public LocalDateTime dateOfOrder() {
        return LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
    }
}
