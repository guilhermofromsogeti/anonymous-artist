package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.model.FileUploadResponse;
import com.sogeti.java.anonymous_artist.repository.FileUploadRepository;
import com.sogeti.java.anonymous_artist.util.FilePathUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class ImageService {

    private final Path fileStoragePath;
    private final FileUploadRepository fileUploadRepository;
    private final String fileStorageLocation;

    public ImageService(@Value("${my.upload_location}") String fileStorageLocation, FileUploadRepository fileUploadRepository) {
        this.fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageLocation = fileStorageLocation;
        this.fileUploadRepository = fileUploadRepository;

        createDirectories(fileStoragePath);
    }

    private void createDirectories(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory", e);
        }
    }

    public String storeFile(MultipartFile file, String url) {
        String fileName = generateFileName(file);
        Path filePath = FilePathUtil.resolveFilePath(fileStorageLocation, fileName);

        copyFileToStorage(file, filePath);
        saveFileMetadata(fileName, file.getContentType(), url);

        return fileName;
    }


    private String generateFileName(MultipartFile file) {
        return StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    }

    private void copyFileToStorage(MultipartFile file, Path filePath) {
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    private void saveFileMetadata(String fileName, String contentType, String url) {
        fileUploadRepository.save(new FileUploadResponse(fileName, contentType, url));
    }

    private Resource loadResource(Path filePath) {
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading the file", e);
        }
    }

    public Resource downLoadFile(String fileName) {
        Path filePath = FilePathUtil.resolveFilePath(fileStorageLocation, fileName);
        Resource resource = loadResource(filePath);

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("The file doesn't exist or is not readable");
        }
    }
}
