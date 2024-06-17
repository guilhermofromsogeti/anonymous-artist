package com.sogeti.java.anonymous_artist.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathUtil {
    public static Path resolveFilePath(String baseLocation, String fileName) {
        return Paths.get(baseLocation).toAbsolutePath().resolve(fileName).normalize();
    }
}
