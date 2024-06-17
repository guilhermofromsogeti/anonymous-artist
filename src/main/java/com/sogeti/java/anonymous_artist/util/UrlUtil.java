package com.sogeti.java.anonymous_artist.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

public class UrlUtil {
    public static String constructFileUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/download/")
                .path(Objects.requireNonNull(filename))
                .toUriString();
    }
}
