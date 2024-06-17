package com.sogeti.java.anonymous_artist.controller;

import com.sogeti.java.anonymous_artist.model.FileUploadResponse;
import com.sogeti.java.anonymous_artist.service.ImageService;
import com.sogeti.java.anonymous_artist.util.MimeTypeUtil;
import com.sogeti.java.anonymous_artist.util.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/anonymous-artist/api/image/")
public class ImageController {

    private final ImageService imageService;
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping("upload")
    public FileUploadResponse singleFileUpload(@RequestParam("file") MultipartFile file) {
        String url = UrlUtil.constructFileUrl(file.getOriginalFilename());
        String contentType = file.getContentType();
        String fileName = imageService.storeFile(file, url);
        return new FileUploadResponse(fileName, contentType, url);
    }

    @GetMapping("download/{fileName}")
    public ResponseEntity<Resource> downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = imageService.downLoadFile(fileName);
        String mimeType = MimeTypeUtil.getMimeType((Resource) resource, request);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
                .body(resource);
    }
}
