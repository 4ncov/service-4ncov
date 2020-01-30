package com.ncov.module;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

@Import({
        DataSourceAutoConfiguration.class,
        FlywayAutoConfiguration.class
})
public class FlywayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FlywayApplication.class).web(WebApplicationType.NONE).run(args);
    }
}
