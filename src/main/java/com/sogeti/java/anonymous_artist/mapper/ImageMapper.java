package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.dto.ImageDto;
import com.sogeti.java.anonymous_artist.model.FileUploadResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageMapper {
    public static ImageDto fromImage(FileUploadResponse image) {
        return new ImageDto(image.getId(), image.getUrl(), image.getContentType(), image.getUrl());
    }
}
