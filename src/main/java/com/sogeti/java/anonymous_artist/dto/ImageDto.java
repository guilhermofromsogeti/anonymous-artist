package com.sogeti.java.anonymous_artist.dto;

import com.sogeti.java.anonymous_artist.model.FileUploadResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class ImageDto {
    private UUID id;
    private String fileName;
    private String contentType;
    private String url;


    public ImageDto(FileUploadResponse image) {
        this.id = image.getId();
        this.fileName = image.getFileName();
        this.contentType = image.getContentType();
        this.url = image.getUrl();
    }
}
