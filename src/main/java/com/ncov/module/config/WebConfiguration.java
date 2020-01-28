package com.ncov.module.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.Arrays;

/**
 * @author JackJun
 * 29/01/2020 01：09
 * Life is not just about survival.
 */
@Configuration
public class WebConfiguration implements ServletContextInitializer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);
    @Inject
    private Environment env;

    @Override
    public void onStartup(ServletContext servletContext) {
        logger.info("Active Profiles {}", Arrays.toString(env.getActiveProfiles()));
    }
}
