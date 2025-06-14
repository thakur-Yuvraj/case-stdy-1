package com.cropdeal.admin.service;

import com.cropdeal.admin.dto.UserDto;
import com.cropdeal.admin.exception.AdminServiceException;
import com.cropdeal.admin.feign.CropServiceClient;
import com.cropdeal.admin.feign.UserServiceClient;
import com.cropdeal.admin.modal.Admin;
import com.cropdeal.admin.repository.AdminRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final CropServiceClient cropServiceClient;
    private final UserServiceClient userServiceClient;
    private final AdminRepository adminRepository;

    public ResponseEntity<String> createAdmin(UserDto userDto) {
        try {
            log.info("Creating admin with email: {}", userDto.getEmail());

            Admin admin = Admin.builder()
                    .email(userDto.getEmail())
                    .fullName(userDto.getFullName())
                    .phone(userDto.getPhone())
                    .adminLevel("SUPER_ADMIN")
                    .canManageUsers(true)
                    .canManageCrops(true)
                    .build();

            adminRepository.save(admin);
            log.info("Admin {} registered successfully", admin.getEmail());

            return ResponseEntity.ok("Admin registered successfully");
        } catch (Exception e) {
            log.error("Error in createAdmin endpoint: {}", e.getMessage());
            throw new AdminServiceException("Failed to create admin");
        }
    }

    public ResponseEntity<String> home() {
        try {
            log.info("Accessing home endpoint for AdminService");
            return ResponseEntity.ok("This is admin service");
        } catch (Exception e) {
            log.error("Error in home endpoint: {}", e.getMessage());
            throw new AdminServiceException("Internal Server Error");
        }
    }

    @Transactional
    public ResponseEntity<String> removeUser(String email) {
        try {
            log.info("Removing user with email: {}", email);
            return userServiceClient.removeUser(email);
        } catch (Exception e) {
            log.error("Error in removeUser endpoint: {}", e.getMessage());
            throw new AdminServiceException("Failed to remove user");
        }
    }

    public ResponseEntity<String> removeCrop(int cropId) {
        try {
            log.info("Removing crop with ID: {}", cropId);
            return cropServiceClient.removeCropById(cropId);
        } catch (Exception e) {
            log.error("Error in removeCrop endpoint: {}", e.getMessage());
            throw new AdminServiceException("Failed to remove crop");
        }
    }
}