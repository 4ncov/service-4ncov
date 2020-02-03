package com.ncov.module.config.swagger;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static com.ncov.module.common.Constants.SPRING_PROFILE_DEVELOPMENT;
import static com.ncov.module.common.Constants.SPRING_PROFILE_LOCAL;

/**
 * @author JackJun
 * 2019/6/27 15:00
 * Life is not just about survival.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Inject
    private Environment env;

    /**
     * 创建RestApi文档，仅在dev环境下创建文档
     */
    @Bean
    public Docket createRestApi() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("4nCoV API")
                .description("4nCov Swagger Api Document")
                .contact(new Contact("Jackjun", "https://4ncov.github.io", "jack@retzero.com"))
                .version("1.0.0").build();

        String activeProfile = env.getActiveProfiles()[0];
        boolean isSwaggerEnabled = Arrays.asList(SPRING_PROFILE_DEVELOPMENT, SPRING_PROFILE_LOCAL)
                .contains(activeProfile);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ncov.module.controller"))
                .paths(PathSelectors.regex("/api/.*"))
                .build().enable(isSwaggerEnabled)
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(securityContexts());
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private List<SecurityContext> securityContexts() {
        return Lists.newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.any())
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Lists.newArrayList(
                new SecurityReference("Authorization", authorizationScopes));
    }
}
