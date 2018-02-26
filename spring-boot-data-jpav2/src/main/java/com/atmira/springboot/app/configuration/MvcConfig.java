package com.atmira.springboot.app.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    private static final Logger log = LoggerFactory.getLogger(MvcConfig.class);
    
    // @Override
    // public void addResourceHandlers(
    // ResourceHandlerRegistry registry){
    // super.addResourceHandlers(registry);
    //
    // String resourcePath =
    // Paths.get("uploads").toAbsolutePath().toUri().toString();
    // log.info("resourcePath: " + resourcePath);
    // registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
    //
    // }
    
}
