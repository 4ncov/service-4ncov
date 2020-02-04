package com.ncov.module.service;

import com.ncov.module.client.JdOssClient;
import com.ncov.module.common.exception.FileUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceTest {

    @Mock
    private JdOssClient jdOssClient;
    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void should_return_image_url_when_upload_image_given_category_and_image_file() throws IOException {
        String expectedUrl = String.format("http://oss.com/images/身份验证-%s.jpeg", UUID.randomUUID().toString());
        when(jdOssClient.uploadImage(anyString(), any(MultipartFile.class))).thenReturn(expectedUrl);

        String imageUrl = fileService.uploadImage("身份验证", new MockMultipartFile("image", "test-image-id.jpeg", "application/octet-stream", new byte[]{}));

        assertEquals(expectedUrl, imageUrl);
    }

    @Test
    void should_upload_the_image_with_correct_name_when_upload_image() throws IOException {
        fileService.uploadImage("物资寻求", new MockMultipartFile("image", "test-image-id.jpeg", "application/octet-stream", new byte[]{}));

        ArgumentCaptor<String> imageNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdOssClient).uploadImage(imageNameCaptor.capture(), ArgumentCaptor.forClass(MultipartFile.class).capture());
        String uploadedImageName = imageNameCaptor.getValue();
        assertTrue(uploadedImageName.startsWith("物资寻求-"));
        assertTrue(uploadedImageName.endsWith(".jpeg"));
    }

    @Test
    void should_throw_file_upload_exception_when_upload_image_given_error_occurred_during_upload() throws IOException {
        when(jdOssClient.uploadImage(anyString(), any(MultipartFile.class))).thenThrow(IOException.class);

        assertThrows(FileUploadException.class, () -> fileService.uploadImage("身份验证", new MockMultipartFile("image", "test-image-id.jpeg", "application/octet-stream", new byte[]{})));
    }
}