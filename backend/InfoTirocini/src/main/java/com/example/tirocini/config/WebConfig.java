package com.example.tirocini.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${my.upload-directory}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String urlMapping = "/immagini-profilo/**"; 

        String physicalPath = "file:" + uploadDir; 

        registry.addResourceHandler(urlMapping)
                .addResourceLocations(physicalPath);
    }
}
