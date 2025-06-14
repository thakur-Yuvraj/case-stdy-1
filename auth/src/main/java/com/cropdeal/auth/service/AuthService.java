package com.cropdeal.auth.service;

import com.cropdeal.auth.dto.UserDto;
import com.cropdeal.auth.exception.AuthServiceException;
import com.cropdeal.auth.feign.UserServiceClient;
import com.cropdeal.auth.modal.Role;
import com.cropdeal.auth.modal.UserCredential;
import com.cropdeal.auth.repository.UserCredentialRepository;
import com.cropdeal.auth.service.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserServiceClient userServiceClient;

    public AuthService(UserCredentialRepository userCredentialRepository,
                       PasswordEncoder passwordEncoder,
                       JWTService jwtService,
                       UserServiceClient userServiceClient) {
        this.userCredentialRepository = userCredentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userServiceClient = userServiceClient;
    }

    public String addUser(UserDto userDto) {
        try {
            logger.info("Registering user with email: {}", userDto.getEmail());

            UserCredential userCredential = UserCredential.builder()
                    .name(userDto.getFullName())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .role(userDto.getRole())
                    .build();

            ResponseEntity<String> response = userServiceClient.register(userDto);
            if (response.getBody() != null && !response.getStatusCode().is2xxSuccessful()) {
                logger.warn("User registration failed: {}", response.getBody());
                return response.getBody();
            }

            userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
            userCredentialRepository.save(userCredential);
            logger.info("User {} registered successfully", userCredential.getEmail());

            return "User added successfully";
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage());
            throw new AuthServiceException("Registration failed");
        }
    }

    public String generateToken(String username, Role role) {
        try {
            logger.info("Generating token for user: {}", username);
            return jwtService.generateToken(username, role);
        } catch (Exception e) {
            logger.error("Token generation failed for user {}: {}", username, e.getMessage());
            throw new AuthServiceException("Token generation failed");
        }
    }

    public void validateToken(String token) {
        try {
            logger.info("Validating token...");
            jwtService.validateToken(token);
            logger.info("Token validation successful");
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            throw new AuthServiceException("Invalid token");
        }
    }

    public String removeUser(String email) {
        try {
            logger.info("Removing user with email: {}", email);
            userServiceClient.removeUser(email);
            userCredentialRepository.deleteByEmail(email);
            logger.info("User {} deleted successfully", email);
            return "User deleted";
        } catch (Exception e) {
            logger.error("Error during user removal: {}", e.getMessage());
            throw new AuthServiceException("User removal failed");
        }
    }
}