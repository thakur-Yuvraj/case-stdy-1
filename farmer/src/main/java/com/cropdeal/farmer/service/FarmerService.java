package com.cropdeal.farmer.service;

import com.cropdeal.farmer.dto.CropDto;
import com.cropdeal.farmer.dto.UserDto;
import com.cropdeal.farmer.feign.CropServiceClient;
import com.cropdeal.farmer.modal.Farmer;
import com.cropdeal.farmer.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private FarmerRepository farmerRepository;

    public ResponseEntity<String> createFarmer(UserDto userDto) {
        try {
            Farmer farmer = Farmer.builder()
                    .email(userDto.getEmail())
                    .fullName(userDto.getFullName())
                    .phone(userDto.getPhone())
                    .bankAccountNumber(null)
                    .ifscCode(null)
                    .build();

            farmerRepository.save(farmer);
            return ResponseEntity.ok("Farmer registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Farmer creation failed");
        }
    }

    public ResponseEntity<String> addCrop(Integer userId, CropDto cropDto) {

        try {
            // Verify user exists and is a farmer
            if(!farmerRepository.existsById(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            Farmer farmer = farmerRepository.findById(userId).get();
            // Set the farmer ID in the crop DTO
            cropDto.setFarmerId(userId);

            // Call crop service via a Feign client
            return cropServiceClient.addCrop(cropDto);
        }catch (Exception e) {
            String errMsg = "Err in FarmerService -> addCrop" + e.getMessage();
            log.error(errMsg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errMsg);
        }

    }

    public ResponseEntity<String> removeCrop(Integer userId, Integer cropId) {
        return cropServiceClient.removeCropByIdAndFarmerId(cropId, userId);
    }

    public ResponseEntity<String> getProfile(int userId) {
        Optional<Farmer> user = farmerRepository.findById(userId);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user exist by this id "+ userId);
        }
        Farmer user1 = user.get();
        String details = user1.toString();
        List<CropDto> cropDetails = cropServiceClient.findByFarmerId(userId).getBody();

        String cropDetail = cropDetails.stream()
                .map(CropDto::toString)
                .reduce((s, s2) -> s + " " + s2)
                .get();


        return ResponseEntity.status(HttpStatus.OK).body(details + " crop Details " + cropDetail);
    }

    public ResponseEntity<String> home() {
        try {
            return ResponseEntity.ok("This is farmer servic3");
        }catch (Exception e) {
            String err = "Err in farmer service -> home " + e.getMessage();
            log.error(err);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    public ResponseEntity<String> removeFarmer(String email) {
        if(!farmerRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("farmer not found");
        }
        farmerRepository.deleteByEmail(email);
        return ResponseEntity.ok("farmer removed");
    }
}
