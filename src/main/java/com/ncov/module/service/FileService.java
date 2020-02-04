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

    public String uploadImage(String category, MultipartFile image) {
        log.info("Uploading image, category=[{}]", category);
        String imageName = getImageName(category, FilenameUtils.getExtension(image.getOriginalFilename()));
        try {
            String imageUrl = jdOssClient.uploadImage(imageName, image);
            log.info("Uploaded image name [{}], imageUrl=[{}]", imageName, imageUrl);
            return imageUrl;
        } catch (IOException e) {
            log.error("Error when uploading image", e);
            throw new FileUploadException();
        }
    }

    private String getImageName(String imageType, String imageExtension) {
        return String.format("%s-%s.%s", imageType, UUID.randomUUID().toString(), imageExtension);
    }
}
