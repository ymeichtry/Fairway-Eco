package bbw.ch.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS configuration for the Gateway.
 * Allows cross-origin requests from configured frontend origins.
 * 
 * Security note: Uses environment-specific allowed origins.
 * - Development: typically http://localhost:3000, http://localhost:5173
 * - Production: your actual frontend domain
 */
@Configuration
public class CorsConfig {

  @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:5173}")
  private String allowedOrigins;

  @Value("${cors.allow-credentials:false}")
  private boolean allowCredentials;

  @Bean
  public CorsWebFilter corsWebFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();

    // Parse comma-separated origins from environment variable
    List<String> origins = Arrays.asList(allowedOrigins.split(","));
    corsConfig.setAllowedOrigins(origins);

    corsConfig.setMaxAge(3600L);
    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
    corsConfig.setAllowCredentials(allowCredentials);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);

    return new CorsWebFilter(source);
  }
}
