package com.sogeti.java.anonymous_artist.dto;

public record ImageDto(
        Long id,
        String fileName,
        String contentType,
        String url
) {
}

