package com.cropdeal.auth.controller;

import com.cropdeal.auth.dto.AuthRequest;
import com.cropdeal.auth.dto.UserDto;
import com.cropdeal.auth.exception.AuthServiceException;
import com.cropdeal.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("register")
    public String addNewUser(@RequestBody UserDto userDto) {
        try {
            logger.info("Register request received for email: {}", userDto.getEmail());
            return authService.addUser(userDto);
        } catch (Exception e) {
            logger.error("Error in register endpoint: {}", e.getMessage());
            throw new AuthServiceException("User registration failed");
        }
    }

    @PostMapping("token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        try {
            logger.info("Token request received for username: {}", authRequest.getName());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                return authService.generateToken(authRequest.getName(), authRequest.getRole());
            }

            logger.warn("Authentication failed for user: {}", authRequest.getName());
            throw new AuthServiceException("Invalid username or password");
        } catch (Exception e) {
            logger.error("Error during authentication: {}", e.getMessage());
            throw new AuthServiceException("Authentication failed");
        }
    }

    @GetMapping("validate")
    public String validateToken(@RequestParam("token") String token) {
        try {
            logger.info("Validating token...");
            authService.validateToken(token);
            return "Token is valid";
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            throw new AuthServiceException("Invalid token");
        }
    }

    @DeleteMapping("/remove/{email}")
    public String removeUser(@PathVariable String email) {
        try {
            logger.info("Remove request received for email: {}", email);
            return authService.removeUser(email);
        } catch (Exception e) {
            logger.error("Error in removeUser endpoint: {}", e.getMessage());
            throw new AuthServiceException("User removal failed");
        }
    }
}