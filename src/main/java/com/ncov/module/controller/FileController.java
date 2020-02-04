package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.file.ImageUploadResponse;
import com.ncov.module.service.FileService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class FileController {

    private FileService fileService;

    @ApiOperation(
            value = "Upload an image.",
            tags = SwaggerConstants.TAG_FILES
    )
    @PostMapping("/images")
    public RestResponse<ImageUploadResponse> uploadImage(@RequestParam String category,
                                                         @RequestParam("image") MultipartFile image) {
        String imageUrl = fileService.uploadImage(category, 1L, image);
        return RestResponse.<ImageUploadResponse>builder()
                .message("Image uploaded.")
                .data(ImageUploadResponse.builder().url(imageUrl).gmtCreated(new Date()).build())
                .build();
    }
}
