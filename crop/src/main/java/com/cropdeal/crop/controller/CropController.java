package com.cropdeal.crop.controller;

import com.cropdeal.crop.dto.CropDto;
import com.cropdeal.crop.exception.CropServiceException;
import com.cropdeal.crop.model.Crop;
import com.cropdeal.crop.service.CropService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crop")
@RequiredArgsConstructor
@Slf4j
public class CropController {

    private final CropService cropService;

    @GetMapping("")
    public ResponseEntity<String> home() {
        try {
            log.info("Accessing CropService home page");
            return cropService.homePage();
        } catch (Exception e) {
            log.error("Error in home endpoint: {}", e.getMessage());
            throw new CropServiceException("Internal Server Error");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCrop(@RequestBody CropDto cropDto) {
        try {
            log.info("Adding new crop: {}", cropDto.getCropName());
            return cropService.addCrop(cropDto);
        } catch (Exception e) {
            log.error("Error in addCrop endpoint: {}", e.getMessage());
            throw new CropServiceException("Failed to add crop");
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeCropByIdAndFarmerId(@PathVariable int id,
                                                            @RequestHeader("X-User-ID") Integer userId) {
        try {
            log.info("Removing crop ID {} for farmer ID {}", id, userId);
            return cropService.removeCropByIdAndFarmerId(id, userId);
        } catch (Exception e) {
            log.error("Error in removeCropByIdAndFarmerId endpoint: {}", e.getMessage());
            throw new CropServiceException("Failed to remove crop");
        }
    }

    @DeleteMapping("/remove/id/{id}")
    public ResponseEntity<String> removeCropById(@PathVariable int id) {
        try {
            log.info("Removing crop with ID {}", id);
            return cropService.removeCropById(id);
        } catch (Exception e) {
            log.error("Error in removeCropById endpoint: {}", e.getMessage());
            throw new CropServiceException("Failed to remove crop");
        }
    }

    @GetMapping("/find/all")
    public ResponseEntity<List<CropDto>> findAllCrop() {
        try {
            log.info("Fetching all crops");
            return cropService.findAllCrop();
        } catch (Exception e) {
            log.error("Error in findAllCrop endpoint: {}", e.getMessage());
            throw new CropServiceException("Failed to fetch crops");
        }
    }

    @GetMapping("/find/all/{farmerId}")
    public ResponseEntity<List<CropDto>> findByFarmerId(@PathVariable int farmerId) {
        try {
            log.info("Fetching crops for farmer ID {}", farmerId);
            return cropService.findByFarmerId(farmerId);
        } catch (Exception e) {
            log.error("Error in findByFarmerId endpoint: {}", e.getMessage());
            throw new CropServiceException("Failed to fetch crops");
        }
    }

    @GetMapping("/find/{cropId}")
    public ResponseEntity<CropDto> findByCropId(@PathVariable int cropId) {
        try {
            log.info("Fetching crop with ID {}", cropId);
            return cropService.findByCropId(cropId);
        } catch (Exception e) {
            log.error("Error in findByCropId endpoint: {}", e.getMessage());
            throw new CropServiceException("Failed to fetch crop");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<String> isValidCrop(@RequestBody Crop crop) {
        try {
            log.info("Validating crop with ID {}", crop.getId());
            return cropService.validateCrop(crop);
        } catch (Exception e) {
            log.error("Error in isValidCrop endpoint: {}", e.getMessage());
            throw new CropServiceException("Failed to validate crop");
        }
    }

    @PostMapping("/makeCropUnavailable/{cropId}")
    public ResponseEntity<String> disableCrop(@PathVariable int cropId) {
        try {
            log.info("Disabling crop with ID {}", cropId);
            return cropService.setAvailabilityToNo(cropId);
        } catch (Exception e) {
            log.error("Error in disableCrop endpoint: {}", e.getMessage());
            throw new CropServiceException("Failed to disable crop");
        }
    }
}