package br.com.brainyit.posystem2.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Value("${cors.allowed-origins-patterns}")
    private String[] allowedOriginPatterns;

    @Value("${cors.mapping}")
    private String mappingPattern;

    @Value("${cors.allowed-methods}")
    private String[] allowedMethods;

    @Value("${cors.allowed-headers}")
    private String[] allowedHeaders;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping(mappingPattern)
                .allowedOriginPatterns(allowedOriginPatterns)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders);
    }
}
