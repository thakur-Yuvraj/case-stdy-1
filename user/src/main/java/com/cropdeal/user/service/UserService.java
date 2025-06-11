package com.cropdeal.user.service;

import com.cropdeal.user.dto.UserDto;
import com.cropdeal.user.modal.*;
import com.cropdeal.user.repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
            // Validate DTO
            if (userRepository.existsByEmail(userDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email already exists");
            }
            if (userDto.getEmail() == null || userDto.getPassword() == null || userDto.getRole() == null || userDto.getFullName() == null || userDto.getPhone() == null || userDto.getAddress() == null) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Please enter valid credential");
            }
            User user;
            switch(userDto.getRole()) {
                case FARMER:
                    user = Farmer.builder()
                            .email(userDto.getEmail())
                            .password(passwordEncoder.encode(userDto.getPassword()))
                            // other common fields
                            .bankAccountNumber(null) // or require in DTO
                            .ifscCode(null)
                            .active(true)
                            .address(userDto.getAddress())
                            .fullName(userDto.getFullName())
                            .phone(userDto.getPhone())
                            .role(userDto.getRole())
                            .build();
                    break;
                case DEALER:
                    user = Dealer.builder()
                            .email(userDto.getEmail())
                            .password(passwordEncoder.encode(userDto.getPassword()))
                            // other common fields
                            .companyName(null) // or require in DTO
                            .gstNumber(null)
                            .businessAddress(userDto.getAddress())
                            .active(true)
                            .address(userDto.getAddress())
                            .fullName(userDto.getFullName())
                            .phone(userDto.getPhone())
                            .role(userDto.getRole())
                            .build();
                    break;
                case ADMIN:
                    // handle admin creation differently
                    user = Admin.builder()
                            .email(userDto.getEmail())
                            .password(passwordEncoder.encode(userDto.getPassword()))
                            .active(true)
                            .address(userDto.getAddress())
                            .fullName(userDto.getFullName())
                            .phone(userDto.getPhone())
                            .role(userDto.getRole())
                            .adminLevel("SUPER_ADMIN")
                            .canManageCrops(true)
                            .canManageUsers(true)
                            .canManageSystemSettings(true)
                            .build();
                    break;
              default:
                    return ResponseEntity.badRequest()
                            .body("Invalid role specified");
            }

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Registered successfully");

        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Registration failed");
        }
    }

}
