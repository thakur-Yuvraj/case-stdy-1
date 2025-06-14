package com.cropdeal.api.filter;

import org.springframework.http.server.reactive.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/token",
            "/api/admin/**",
            "/eureka"
    );

    private RouteValidator(){}

    public static final Predicate<ServerHttpRequest> isSecure =
            serverHttpRequest -> openApiEndpoints
                    .stream()
                    .noneMatch(url -> serverHttpRequest.getURI().getPath().contains(url));
}
