package com.cpp.urlshorter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
public class UrlshorterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlshorterApplication.class, args);
    }
}
