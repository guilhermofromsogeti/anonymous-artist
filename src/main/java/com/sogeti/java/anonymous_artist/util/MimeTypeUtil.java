package com.sogeti.java.anonymous_artist.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.IOException;

public class MimeTypeUtil {
    public static String getMimeType(Resource resource, HttpServletRequest request) {
        try {
            return request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

    }
}
