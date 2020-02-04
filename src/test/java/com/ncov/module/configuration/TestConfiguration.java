package com.ncov.module.configuration;

import com.ncov.module.client.JdOssClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.mockito.Mockito.*;

@Configuration
@Profile("test")
public class TestConfiguration {

    @Bean
    public JdOssClient jdOssClient() throws IOException {
        JdOssClient mock = Mockito.mock(JdOssClient.class);
        when(mock.uploadImage(anyString(), any(MultipartFile.class))).thenReturn("https://oss.com/images/身份验证-4cccf5ed-8e04-42b7-a8a1-0fce2cdb1b0e.png");
        return mock;
    }
}
