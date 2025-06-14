package com.cropdeal.api.filter;

import com.cropdeal.api.exception.UnauthorizedAccessException;
import com.cropdeal.api.util.JWTUtil;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
    private final JWTUtil jwtUtil;

    public AuthFilter(JWTUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (RouteValidator.isSecure.test(exchange.getRequest()) &&
                    !exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new UnauthorizedAccessException("Missing auth header");
            }


            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }

            try {
                String userRole = jwtUtil.extractUserRole(authHeader); // Extract role from JWT
                log.info("User Role: {}", userRole);

                // Role-based access restriction
                String path = exchange.getRequest().getURI().getPath();
                userRoleValidation(path, userRole);

            } catch (Exception e) {
                throw new UnauthorizedAccessException("Unauthorized access");
            }
            return chain.filter(exchange);
        });
    }

    private void userRoleValidation(String path, String userRole) {
        if (path.contains("/api/admin") && !userRole.equals("ADMIN")) {
            throw new UnauthorizedAccessException("Admin role required.");
        }
        if (path.contains("/api/dealer") && !userRole.equals("DEALER")) {
            throw new UnauthorizedAccessException("Dealer role required.");
        }
        if (path.contains("/api/farmer") && !userRole.equals("FARMER")) {
            throw new UnauthorizedAccessException("Farmer role required.");
        }
    }

    @Component
    public static class Config{

    }
}
