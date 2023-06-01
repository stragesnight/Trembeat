package com.trembeat.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Additional web configuration class
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {
    public static final int PAGE_LEN = 10;


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Add access to user-uploaded files
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
