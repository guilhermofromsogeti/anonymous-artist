package com.sogeti.java.anonymous_artist.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AddressRequest(
        @NotBlank(message = "Please fill in your street name.")
        @Size(min = 3, max = 100, message = "Street name should be between 3 and 100 characters.")
        String streetName,

        @NotEmpty
        @Pattern(regexp = "^(?:[1-9]\\d{0,2}|1000)$", message = "House number should be between 1 and 1000.")
        String houseNumber,

        String houseNumberAddition,

        @NotBlank(message = "Please fill in your zip code.")
        String zipCode,

        @NotBlank(message = "Please fill in the name of your city.")
        @Size(min = 3, max = 100, message = "City name should be between 3 and 100 characters.")
        String city,

        @NotBlank(message = "Please fill in the name of your province.")
        @Size(min = 3, max = 100, message = "Province name should be between 3 and 100 characters.")
        String province,

        @NotBlank(message = "Please fill in the name of your country.")
        @Size(min = 5, max = 100, message = "Country name should be between 5 and 100 characters.")
        String country
) {
}
