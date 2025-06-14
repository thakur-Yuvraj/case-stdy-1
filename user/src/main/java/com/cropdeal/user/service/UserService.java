package com.cropdeal.user.service;

import com.cropdeal.user.dto.UserDto;
import com.cropdeal.user.exception.UserServiceException;
import com.cropdeal.user.feign.AdminServiceClient;
import com.cropdeal.user.feign.DealerServiceClient;
import com.cropdeal.user.feign.FarmerServiceClient;
import com.cropdeal.user.modal.User;
import com.cropdeal.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FarmerServiceClient farmerServiceClient;
    private final DealerServiceClient dealerServiceClient;
    private final AdminServiceClient adminServiceClient;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       FarmerServiceClient farmerServiceClient,
                       DealerServiceClient dealerServiceClient,
                       AdminServiceClient adminServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.farmerServiceClient = farmerServiceClient;
        this.dealerServiceClient = dealerServiceClient;
        this.adminServiceClient = adminServiceClient;
    }

    public ResponseEntity<String> home() {
        try {
            return ResponseEntity.ok("This is user-service");
        } catch (Exception e) {
            log.error("Error in UserService -> home: {}", e.getMessage());
            throw new UserServiceException("Internal Server Error");
        }
    }

    public ResponseEntity<String> register(UserDto userDto) {
        try {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email already exists");
            }

            log.info("Registering new user with email: {}", userDto.getEmail());

            User user = User.builder()
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .active(true)
                    .fullName(userDto.getFullName())
                    .phone(userDto.getPhone())
                    .role(userDto.getRole())
                    .address(userDto.getAddress())
                    .build();

            userRepository.save(user);
            log.info("User {} registered successfully", user.getEmail());

            // Delegate role-specific creation using Feign Clients
            switch (userDto.getRole()) {
                case FARMER -> farmerServiceClient.createFarmer(userDto);
                case DEALER -> dealerServiceClient.createDealer(userDto);
                case ADMIN -> adminServiceClient.createAdmin(userDto);
            }

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            throw new UserServiceException("Registration failed");
        }
    }

    @Transactional
    public ResponseEntity<String> removeUser(String email) {
        try {
            if (!userRepository.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not found");
            }

            User user = userRepository.findByEmail(email).orElseThrow(() ->
                    new UserServiceException("User not found"));

            log.info("Removing user with email: {}", email);

            switch (user.getRole()) {
                case FARMER -> farmerServiceClient.removeFarmer(email);
                case DEALER -> dealerServiceClient.removeDealer(email);
                case ADMIN -> {
                    log.warn("Attempt to delete admin account");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin cannot be deleted");
                }
            }
            userRepository.deleteByEmail(email);
            log.info("User {} deleted successfully", email);

            return ResponseEntity.ok("User Deleted Successfully");
        } catch (Exception e) {
            log.error("Error in UserService -> removeUser: {}", e.getMessage());
            throw new UserServiceException("Error while removing user");
        }
    }
}