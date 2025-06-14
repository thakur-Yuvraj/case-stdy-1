package com.cropdeal.farmer.controller;

import com.cropdeal.farmer.dto.CropDto;
import com.cropdeal.farmer.dto.UserDto;
import com.cropdeal.farmer.exception.FarmerServiceException;
import com.cropdeal.farmer.service.FarmerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farmer")
@RequiredArgsConstructor
@Slf4j
public class FarmerController {

    private final FarmerService farmerService;

    @GetMapping("")
    public ResponseEntity<String> home() {
        try {
            log.info("Accessing home endpoint for FarmerService");
            return farmerService.home();
        } catch (Exception e) {
            log.error("Error in home endpoint: {}", e.getMessage());
            throw new FarmerServiceException("Internal Server Error");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> addFarmer(@RequestBody UserDto userDto) {
        try {
            log.info("Creating farmer with email: {}", userDto.getEmail());
            return farmerService.createFarmer(userDto);
        } catch (Exception e) {
            log.error("Error in createFarmer endpoint: {}", e.getMessage());
            throw new FarmerServiceException("Failed to create farmer");
        }
    }

    @PostMapping("/crop")
    public ResponseEntity<String> addCrop(@RequestBody CropDto cropDto,
                                          @RequestHeader("X-User-ID") Integer userId) {
        try {
            log.info("Adding crop for farmer ID: {}", userId);
            return farmerService.addCrop(userId, cropDto);
        } catch (Exception e) {
            log.error("Error in addCrop endpoint: {}", e.getMessage());
            throw new FarmerServiceException("Failed to add crop");
        }
    }

    @DeleteMapping("/crop/{cropId}")
    public ResponseEntity<String> removeCrop(@PathVariable Integer cropId,
                                             @RequestHeader("X-User-ID") Integer userId) {
        try {
            log.info("Removing crop ID {} for farmer ID {}", cropId, userId);
            return farmerService.removeCrop(userId, cropId);
        } catch (Exception e) {
            log.error("Error in removeCrop endpoint: {}", e.getMessage());
            throw new FarmerServiceException("Failed to remove crop");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(@RequestHeader("X-User-ID") int userId) {
        try {
            log.info("Fetching profile for farmer ID: {}", userId);
            return farmerService.getProfile(userId);
        } catch (Exception e) {
            log.error("Error in getProfile endpoint: {}", e.getMessage());
            throw new FarmerServiceException("Failed to retrieve profile");
        }
    }

    @DeleteMapping("/remove/{email}")
    public ResponseEntity<String> removeFarmer(@PathVariable String email) {
        try {
            log.info("Removing farmer with email: {}", email);
            return farmerService.removeFarmer(email);
        } catch (Exception e) {
            log.error("Error in removeFarmer endpoint: {}", e.getMessage());
            throw new FarmerServiceException("Failed to remove farmer");
        }
    }
}