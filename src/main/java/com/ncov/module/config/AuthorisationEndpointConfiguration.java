package com.ncov.module.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("security")
@Getter
@Setter
public class AuthorisationEndpointConfiguration {

    private String jwtSecret;
    private Long jwtExpirationInMs;
    private List<EndpointIgnoreToken> endpointsIgnoreToken;

    @ToString
    @Getter
    @Setter
    public static class EndpointIgnoreToken {

        private String path;
        private List<String> methods;
    }
}
