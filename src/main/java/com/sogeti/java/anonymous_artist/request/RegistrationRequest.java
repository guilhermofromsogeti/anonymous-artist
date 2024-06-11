package com.sogeti.java.anonymous_artist.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @Valid
    @NotNull(message = "Please fill in your first name, infix (optional), last name, date of birth and phone number.")
    private UserRequest userRequest;
    @Valid
    @NotNull(message = "Please fill in an email address and password.")
    private AccountRequest accountRequest;
    @Valid
    @NotNull(message = "Please fill in your street name, house number, zipcode, city, province and country.")
    private AddressRequest addressRequest;
}
