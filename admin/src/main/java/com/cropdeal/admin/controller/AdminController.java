package com.cropdeal.admin.controller;

import com.cropdeal.admin.dto.UserDto;
import com.cropdeal.admin.exception.AdminServiceException;
import com.cropdeal.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("")
    public ResponseEntity<String> home() {
        try {
            log.info("Accessing home endpoint for AdminService");
            return adminService.home();
        } catch (Exception e) {
            log.error("Error in home endpoint: {}", e.getMessage());
            throw new AdminServiceException("Internal Server Error");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> addAdmin(@RequestBody UserDto userDto) {
        try {
            log.info("Creating admin with email: {}", userDto.getEmail());
            return adminService.createAdmin(userDto);
        } catch (Exception e) {
            log.error("Error in createAdmin endpoint: {}", e.getMessage());
            throw new AdminServiceException("Failed to create admin");
        }
    }

    @DeleteMapping("/remove/user/{email}")
    public ResponseEntity<String> removeUser(@PathVariable String email) {
        try {
            log.info("Removing user with email: {}", email);
            return adminService.removeUser(email);
        } catch (Exception e) {
            log.error("Error in removeUser endpoint: {}", e.getMessage());
            throw new AdminServiceException("Failed to remove user");
        }
    }

    @DeleteMapping("/remove/crop/{cropId}")
    public ResponseEntity<String> removeCrop(@PathVariable int cropId) {
        try {
            log.info("Removing crop with ID: {}", cropId);
            return adminService.removeCrop(cropId);
        } catch (Exception e) {
            log.error("Error in removeCrop endpoint: {}", e.getMessage());
            throw new AdminServiceException("Failed to remove crop");
        }
    }
}