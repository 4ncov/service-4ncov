package com.ncov.module.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@Slf4j
@Profile("!test")
public class JdOssClient {

    private AmazonS3 s3;
    private String bucketName;
    private String ossEndpoint;

    public JdOssClient(@Value("${oss.endpoint}") String ossEndpoint,
                       @Value("${oss.region}") String ossRegion,
                       @Value("${oss.bucketName}") String bucketName,
                       @Value("${oss.accessKey}") String ossAccessKey,
                       @Value("${oss.secretKey}") String ossSecretKey) {
        this.bucketName = bucketName;
        this.ossEndpoint = ossEndpoint;
        ClientConfiguration config = new ClientConfiguration();
        AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(ossEndpoint, ossRegion);
        BasicAWSCredentials credentials = new BasicAWSCredentials(ossAccessKey, ossSecretKey);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        s3 = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(config)
                .withCredentials(credentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(true)
                .build();
    }

    public String uploadImage(String imageName, MultipartFile imageFile) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());
        s3.putObject(bucketName, "images/" + imageName, imageFile.getInputStream(), metadata);
        return String.format("%s/%s/images/%s", ossEndpoint, bucketName, imageName);
    }
}
