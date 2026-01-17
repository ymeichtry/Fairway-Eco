package bbw.ch.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * Gateway routing configuration.
 * Defines routes to backend microservices.
 */
@Configuration
public class GatewayConfig {

    @Value("${backend.service.url:http://backend:8080}")
    private String backendServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Golf Ball API Routes
                .route("golfball_route", r -> r
                        .path("/api/golfballs/**", "/api/v1/golf-balls/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .circuitBreaker(c -> c
                                        .setName("golfballCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/golfballs"))
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET)))
                        .uri(backendServiceUrl))

                // Customer API Routes
                .route("customer_route", r -> r
                        .path("/api/customers/**", "/api/v1/customers/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .circuitBreaker(c -> c
                                        .setName("customerCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/customers"))
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET)))
                        .uri(backendServiceUrl))

                // Order API Routes
                .route("order_route", r -> r
                        .path("/api/orders/**", "/api/v1/orders/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .circuitBreaker(c -> c
                                        .setName("orderCircuitBreaker")
                                        .setFallbackUri("forward:/fallback/orders"))
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET, HttpMethod.POST)))
                        .uri(backendServiceUrl))

                // Health Check Route
                .route("health_route", r -> r
                        .path("/actuator/health")
                        .filters(f -> f.stripPrefix(0))
                        .uri(backendServiceUrl))

                .build();
    }
}
