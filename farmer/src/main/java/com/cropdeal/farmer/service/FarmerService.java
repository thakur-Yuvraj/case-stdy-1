package com.cropdeal.farmer.service;

import com.cropdeal.farmer.dto.CropDto;
import com.cropdeal.farmer.dto.UserDto;
import com.cropdeal.farmer.exception.FarmerServiceException;
import com.cropdeal.farmer.feign.CropServiceClient;
import com.cropdeal.farmer.modal.Farmer;
import com.cropdeal.farmer.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FarmerService {
    private final CropServiceClient cropServiceClient;
    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> createFarmer(UserDto userDto) {
        try {
            log.info("Creating farmer with email: {}", userDto.getEmail());

            Farmer farmer = Farmer.builder()
                    .email(userDto.getEmail())
                    .fullName(userDto.getFullName())
                    .phone(userDto.getPhone())
                    .bankAccountNumber(null)
                    .ifscCode(null)
                    .build();

            farmerRepository.save(farmer);
            log.info("Farmer {} registered successfully", farmer.getEmail());

            return ResponseEntity.ok("Farmer registered successfully");
        } catch (Exception e) {
            log.error("Farmer creation failed: {}", e.getMessage());
            throw new FarmerServiceException("Failed to create farmer");
        }
    }

    public ResponseEntity<String> addCrop(Integer userId, CropDto cropDto) {
        try {
            log.info("Adding crop for farmer ID: {}", userId);

            if (!farmerRepository.existsById(userId)) {
                log.warn("Farmer with ID {} not found", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            cropDto.setFarmerId(userId);
            return cropServiceClient.addCrop(cropDto);
        } catch (Exception e) {
            log.error("Error in FarmerService -> addCrop: {}", e.getMessage());
            throw new FarmerServiceException("Failed to add crop");
        }
    }

    public ResponseEntity<String> removeCrop(Integer userId, Integer cropId) {
        try {
            log.info("Removing crop ID {} for farmer ID {}", cropId, userId);
            return cropServiceClient.removeCropByIdAndFarmerId(cropId, userId);
        } catch (Exception e) {
            log.error("Error in FarmerService -> removeCrop: {}", e.getMessage());
            throw new FarmerServiceException("Failed to remove crop");
        }
    }

    public ResponseEntity<String> getProfile(int userId) {
        try {
            log.info("Fetching profile for farmer ID: {}", userId);
            Optional<Farmer> user = farmerRepository.findById(userId);

            if (user.isEmpty()) {
                log.warn("Farmer with ID {} not found", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user exists by this ID " + userId);
            }

            Farmer user1 = user.get();
            List<CropDto> cropDetails = cropServiceClient.findByFarmerId(userId).getBody();

            String cropDetail = cropDetails.stream()
                    .map(CropDto::toString)
                    .reduce((s, s2) -> s + " " + s2)
                    .orElse("No crops found");

            return ResponseEntity.ok(user1.toString() + " Crop Details: " + cropDetail);
        } catch (Exception e) {
            log.error("Error fetching farmer profile: {}", e.getMessage());
            throw new FarmerServiceException("Failed to retrieve profile");
        }
    }

    public ResponseEntity<String> home() {
        try {
            log.info("Accessing home endpoint for FarmerService");
            return ResponseEntity.ok("This is Farmer Service");
        } catch (Exception e) {
            log.error("Error in FarmerService -> home: {}", e.getMessage());
            throw new FarmerServiceException("Internal Server Error");
        }
    }

    public ResponseEntity<String> removeFarmer(String email) {
        try {
            log.info("Removing farmer with email: {}", email);

            if (!farmerRepository.existsByEmail(email)) {
                log.warn("Farmer not found: {}", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Farmer not found");
            }

            farmerRepository.deleteByEmail(email);
            log.info("Farmer {} removed successfully", email);

            return ResponseEntity.ok("Farmer removed");
        } catch (Exception e) {
            log.error("Error in FarmerService -> removeFarmer: {}", e.getMessage());
            throw new FarmerServiceException("Failed to remove farmer");
        }
    }
}