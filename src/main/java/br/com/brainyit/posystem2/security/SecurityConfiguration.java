package br.com.brainyit.posystem2.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!test")
public class SecurityConfiguration {

        @Value("${security.api.non-protected-apis}")
        private String[] nonProtectedApis;

        @Value("${security.api.protected-apis}")
        private String[] protectedApis;

        @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
        private String jwtIssuerUri;

        @Value("${cors.allowed-origins-patterns}")
        private String[] allowedOriginPatterns;

        @Value("${cors.mapping}")
        private String mappingPattern;

        @Value("${cors.allowed-methods}")
        private String[] allowedMethods;

        @Value("${cors.allowed-headers}")
        private String[] allowedHeaders;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .authorizeHttpRequests(
                                                (authorize) -> authorize.requestMatchers(nonProtectedApis).permitAll())
                                .authorizeHttpRequests(
                                                (authorize) -> authorize.requestMatchers(protectedApis).authenticated())
                                .oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer
                                                .jwt((jwt) -> jwt
                                                                .decoder(jwtDecoder())))
                                .sessionManagement(
                                                session -> session.sessionCreationPolicy(
                                                                SessionCreationPolicy.STATELESS));

                return http.build();
        }

        @Bean
        public JwtDecoder jwtDecoder() {
                return NimbusJwtDecoder.withIssuerLocation(jwtIssuerUri).build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                final var configuration = new CorsConfiguration();
                configuration.setAllowedOriginPatterns(Arrays.asList(allowedOriginPatterns));
                configuration.setAllowedMethods(Arrays.asList(allowedMethods));
                configuration.setAllowedHeaders(Arrays.asList(allowedHeaders));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration(mappingPattern, configuration);
                return source;
        }

}
