package com.rest.api.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by vbarros on 16/09/2019 .
 */
@SpringBootApplication
public class RestApiExampleApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RestApiExampleApplication.class);
    }

    public static void main(String... args) {
        SpringApplication.run(RestApiExampleApplication.class, args);
    }

}
