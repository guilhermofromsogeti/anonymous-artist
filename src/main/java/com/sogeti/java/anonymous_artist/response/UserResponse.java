package com.sogeti.java.anonymous_artist.response;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponse(
        UUID membershipId,
        String firstName,
        String infix,
        String lastName,
        LocalDate dateOfBirth,
        String phoneNumber
) {
}
