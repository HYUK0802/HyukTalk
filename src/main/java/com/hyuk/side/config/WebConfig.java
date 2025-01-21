package com.hyuk.side.config;

import com.hyuk.side.compents.FileStorageProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageProperties fileStorageProperties;

    public WebConfig(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/chat-images/**")
                .addResourceLocations("file:/Users/hyuk/Desktop/images/chat-images/");

        registry.addResourceHandler("/uploads/profile-images/**")
                .addResourceLocations("file:/Users/hyuk/Desktop/images/profile-images/");
    }

}

