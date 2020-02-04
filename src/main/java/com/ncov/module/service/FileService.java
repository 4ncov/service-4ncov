package com.ncov.module.service;

import com.ncov.module.client.JdOssClient;
import com.ncov.module.common.exception.FileUploadException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class FileService {

    private JdOssClient jdOssClient;

    public String uploadImage(String category, Long uploaderId, MultipartFile image) {
        log.info("Uploading image, category=[{}], uploaderId=[{}]", category, uploaderId);
        String imageName = getImageName(category, uploaderId, FilenameUtils.getExtension(image.getOriginalFilename()));
        try {
            String imageUrl = jdOssClient.uploadImage(imageName, image);
            log.info("Uploaded image name [{}], imageUrl=[{}]", imageName, imageUrl);
            return imageUrl;
        } catch (IOException e) {
            log.error("Error when uploading image", e);
            throw new FileUploadException();
        }
    }

    private String getImageName(String imageType, Long uploaderId, String imageExtension) {
        return String.format("%s-%s-%s.%s", imageType, uploaderId, UUID.randomUUID().toString(), imageExtension);
    }
}
