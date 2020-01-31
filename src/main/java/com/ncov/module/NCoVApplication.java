package com.ncov.module;

import com.ncov.module.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class NCoVApplication extends SpringBootServletInitializer {

    private static Logger logger = LoggerFactory.getLogger(NCoVApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        if (System.getProperty(Constants.SPRING_PROFILE_KEY) == null) {
            System.setProperty(Constants.SPRING_PROFILE_KEY, Constants.SPRING_PROFILE_LOCAL);
        }
        SpringApplication app = new SpringApplication(NCoVApplication.class);
        Environment env = app.run(args).getEnvironment();
        logger.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        if (System.getProperty(Constants.SPRING_PROFILE_KEY) == null) {
            System.setProperty(Constants.SPRING_PROFILE_KEY, Constants.SPRING_PROFILE_LOCAL);
        }
        builder.sources(this.getClass());
        return super.configure(builder);
    }
}
