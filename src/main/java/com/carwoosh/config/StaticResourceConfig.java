package com.carwoosh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve /uploads/** URLs from the "uploads" folder in project root
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/uploads/")
                .setCachePeriod(3600); // optional cache in seconds
    }
}

