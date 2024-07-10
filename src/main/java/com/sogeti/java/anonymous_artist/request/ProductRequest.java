package com.sogeti.java.anonymous_artist.request;

import com.sogeti.java.anonymous_artist.dto.ImageDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record ProductRequest(
        UUID id,
        @NotBlank(message = "Title must not be blank, please enter a title")
        String title,
        String smallSummary,
        String description,
        @NotNull(message = "Price must not be empty, please enter a price")
        @Positive(message = "Please enter a price bigger then 0.")
        BigDecimal price,
        //todo: change amount to enable amount to zero.
        @Positive(message = "Please enter an amount bigger then 0.")
        Integer amountInStock,
        List<ImageDto> imageDto
) {
}