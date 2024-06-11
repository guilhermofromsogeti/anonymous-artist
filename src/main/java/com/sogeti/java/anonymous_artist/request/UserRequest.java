package com.sogeti.java.anonymous_artist.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sogeti.java.anonymous_artist.model.Cart;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserRequest(
        @NotBlank(message = "Please fill in your first name.")
        String firstName,

        String infix,

        @NotBlank(message = "Please fill in your last name.")
        String lastName,

        @Past(message = "Please fill in a valid date of birth in the past. Format: dd-MM-yyyy")
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dateOfBirth,

        @NotBlank(message = "Please fill in your phone number.")
        String phoneNumber,
        Cart cart
) {
}
