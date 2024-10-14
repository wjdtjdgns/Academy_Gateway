package com.nhnacademy.miniDooray.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteLocatorConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("member-service", r -> r.path("/members/**")
                        .uri("lb://MEMBER-SERVICE"))

                .route("project-service", r -> r.path("/projects/**")
                        .uri("lb://PROJECT-SERVICE"))

                .build();
    }
}
