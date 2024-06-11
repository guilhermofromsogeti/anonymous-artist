package com.sogeti.java.anonymous_artist.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record AccountRequest(
        @NotBlank(message = "Please fill in your email address. Example: example@example.com")
        @Email(message = "Please fill in a valid email address. Example: example@example.com")
        String email,

        @NotBlank(message = "Please fill in a password.")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,15}$",
                message = "Password must be 8 to 15 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character.")
        String password
) {
}
