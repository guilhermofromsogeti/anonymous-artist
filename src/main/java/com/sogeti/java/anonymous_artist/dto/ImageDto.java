package com.sogeti.java.anonymous_artist.dto;

import java.util.UUID;


public record ImageDto(
        UUID id,
        String fileName,
        String contentType,
        String url
) {
}

