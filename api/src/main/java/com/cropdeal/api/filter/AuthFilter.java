package com.cropdeal.api.filter;

import com.cropdeal.api.feign.AuthServiceClient;
import com.cropdeal.api.util.JWTUtil;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    @Autowired
    RouteValidator routeValidator;
    @Autowired
    JWTUtil jwtUtil;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            if(routeValidator.isSecure.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing auth header");
                }
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }

            try {
                String userRole = jwtUtil.extractUserRole(authHeader); // Extract role from JWT
                System.out.println("User Role: " + userRole);

                // Role-based access restriction
                if(exchange.getRequest().getURI().getPath().contains("/api/admin") && !userRole.equals("ADMIN")) {
                    throw new RuntimeException("Unauthorized access: Admin role required.");
                }
                if(exchange.getRequest().getURI().getPath().contains("/api/dealer") && !userRole.equals("DEALER")) {
                    throw new RuntimeException("Unauthorized access: Dealer role required.");
                }
                if(exchange.getRequest().getURI().getPath().contains("/api/farmer") && !userRole.equals("FARMER")) {
                    throw new RuntimeException("Unauthorized access: Farmer role required.");
                }
            } catch (Exception e) {
                throw new RuntimeException("Unauthorized access");
            }

            return chain.filter(exchange);
        }));
    }

//    @Override
//    public GatewayFilter apply(Config config) {
//        return (((exchange, chain) -> {
//            System.out.println("Chain running");
//            if(routeValidator.isSecure.test(exchange.getRequest())) {
//                // checking header contains header or not
//                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("missing auth header");
//                }
//            }
//            String authHeader = exchange.getRequest().getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
//            if(authHeader !=null && authHeader.startsWith("Bearer ")) {
//                authHeader= authHeader.substring(7);
//            }
//            try {
//                // rest call to auth service
////                String responseBody = authServiceClient.validateToken(authHeader);
//                jwtUtil.validateToken(authHeader);
//            }catch (Exception e) {
//                System.out.println("invalid access...!");
//                throw new RuntimeException("unAuthorization access to app token " + authHeader);
//            }
//            return chain.filter(exchange);
//        }));
//    }

    public static class Config{

    }
}
