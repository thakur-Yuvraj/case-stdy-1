package com.cropdeal.user.service;

import com.cropdeal.user.dto.UserDto;
import com.cropdeal.user.feign.AdminServiceClient;
import com.cropdeal.user.feign.DealerServiceClient;
import com.cropdeal.user.feign.FarmerServiceClient;
import com.cropdeal.user.modal.*;
import com.cropdeal.user.repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FarmerServiceClient farmerServiceClient;

    @Autowired
    private DealerServiceClient dealerServiceClient;

    @Autowired
    private AdminServiceClient adminServiceClient;

    public ResponseEntity<String> home() {
        try {
            return ResponseEntity.ok("This is user-service");
        } catch (Exception e) {
            String msg = "Err in user service -> home " + e.getMessage();
            System.out.println(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    public ResponseEntity<String> register(UserDto userDto) {
        try {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email already exists");
            }
            System.out.println("here");
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
            System.out.println("Hello");
            // Delegate role-specific creation using Feign Clients
            switch (userDto.getRole()) {
                case FARMER -> farmerServiceClient.createFarmer(userDto);
                case DEALER -> {
                    System.out.println(dealerServiceClient.createDealer(userDto)); }
                case ADMIN -> adminServiceClient.createAdmin(userDto);
            }

            return ResponseEntity.ok("User registered successfully");

        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed");
        }
    }

    @Transactional
    public ResponseEntity<String> removeUser(String email) {
        try {
            if(!userRepository.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not found");
            }
            switch (userRepository.findByEmail(email).get().getRole()) {
                case FARMER -> farmerServiceClient.removeFarmer(email);
                case DEALER -> dealerServiceClient.removeDealer(email);
                case ADMIN -> {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin cannot be deleted");
                }
            }

            userRepository.deleteByEmail(email);
            // call the other service based on user type
            // todo

            return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully");
        }catch (Exception e) {
            String err ="Err in Admin Service -> remove user " + e.getMessage();

            log.error(err);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

}
