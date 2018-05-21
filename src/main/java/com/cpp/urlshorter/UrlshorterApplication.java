package com.cpp.urlshorter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
@EnableSwagger2
public class UrlshorterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlshorterApplication.class, args);
    }
}
