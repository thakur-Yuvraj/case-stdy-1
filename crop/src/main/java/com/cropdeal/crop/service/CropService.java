package com.cropdeal.crop.service;

import com.cropdeal.crop.dto.CropDto;
import com.cropdeal.crop.exception.CropServiceException;
import com.cropdeal.crop.model.Crop;
import com.cropdeal.crop.repository.CropRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CropService {

    private final CropRepository cropRepository;

    public ResponseEntity<String> homePage() {
        try {
            log.info("Accessing home page of CropService");
            return ResponseEntity.ok("This is homepage");
        } catch (Exception e) {
            log.error("Error in homePage function: {}", e.getMessage());
            throw new CropServiceException("Internal Server Error");
        }
    }

    public ResponseEntity<String> addCrop(CropDto cropDto) {
        try {
            log.info("Adding new crop: {}", cropDto.getCropName());

            Crop crop = Crop.builder()
                    .farmerId(cropDto.getFarmerId())
                    .cropType(cropDto.getCropType())
                    .cropName(cropDto.getCropName())
                    .quantity(cropDto.getQuantity())
                    .pricePerUnit(cropDto.getPricePerUnit())
                    .address(cropDto.getAddress())
                    .isAvailable(cropDto.getIsAvailable())
                    .build();

            if (cropRepository.existsById(crop.getId())) {
                log.warn("Crop with ID {} already exists", crop.getId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Crop with ID " + crop.getId() + " already exists");
            }

            cropRepository.save(crop);
            log.info("Crop {} added successfully", cropDto.getCropName());
            return ResponseEntity.ok("Crop added successfully");
        } catch (Exception e) {
            log.error("Error in addCrop function: {}", e.getMessage());
            throw new CropServiceException("Failed to add crop");
        }
    }

    public ResponseEntity<String> removeCropByIdAndFarmerId(int cropId, int farmerId) {
        try {
            log.info("Removing crop ID {} for farmer ID {}", cropId, farmerId);

            if (!cropRepository.existsByIdAndFarmerId(cropId, farmerId)) {
                log.warn("Crop not found with ID {}", cropId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop not found by ID " + cropId);
            }

            cropRepository.deleteById(cropId);
            log.info("Crop with ID {} deleted successfully", cropId);
            return ResponseEntity.ok("Crop deleted successfully");
        } catch (Exception e) {
            log.error("Error in removeCropById function: {}", e.getMessage());
            throw new CropServiceException("Failed to remove crop");
        }
    }

    public ResponseEntity<List<CropDto>> findAllCrop() {
        try {
            log.info("Fetching all crops");
            List<CropDto> crops = cropRepository.findAll().stream()
                    .map(crop -> new CropDto(crop.getFarmerId(), crop.getCropType(), crop.getCropName(),
                            crop.getQuantity(), crop.getPricePerUnit(), crop.getAddress(), crop.getIsAvailable()))
                    .toList();

            return ResponseEntity.ok(crops);
        } catch (Exception e) {
            log.error("Error in findAllCrop function: {}", e.getMessage());
            throw new CropServiceException("Failed to fetch crops");
        }
    }

    public ResponseEntity<String> setAvailabilityToNo(int cropId) {
        try {
            log.info("Setting availability of crop ID {} to No", cropId);

            if (cropRepository.existsById(cropId)) {
                Crop crop = cropRepository.findById(cropId).orElseThrow(() -> new CropServiceException("Crop not found"));
                crop.setIsAvailable("No");
                cropRepository.save(crop);
                log.info("Crop ID {} availability set to No", cropId);
                return ResponseEntity.ok("Crop availability updated successfully");
            } else {
                log.warn("Crop ID {} not found", cropId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop not found");
            }
        } catch (Exception e) {
            log.error("Error in setAvailabilityToNo function: {}", e.getMessage());
            throw new CropServiceException("Failed to update crop availability");
        }
    }

    @Transactional
    public ResponseEntity<String> removeCropById(int id) {
        try {
            log.info("Removing crop with ID: {}", id);

            if (!cropRepository.existsById(id)) {
                log.warn("Crop ID {} not found", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop not found");
            }

            cropRepository.deleteById(id);
            log.info("Crop ID {} removed successfully", id);
            return ResponseEntity.ok("Crop deleted successfully");
        } catch (Exception e) {
            log.error("Error in removeCropById function: {}", e.getMessage());
            throw new CropServiceException("Failed to remove crop");
        }
    }

    public ResponseEntity<String> validateCrop(Crop crop) {
        try {
            log.info("Validating crop with ID: {}", crop.getId());

            if (!cropRepository.existsById(crop.getId())) {
                log.warn("Crop ID {} not found", crop.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Validation Failed");
            }

            log.info("Crop ID {} validation successful", crop.getId());
            return ResponseEntity.ok("Validation Successful");
        } catch (Exception e) {
            log.error("Error in validateCrop function: {}", e.getMessage());
            throw new CropServiceException("Failed to validate crop");
        }
    }

    public ResponseEntity<List<CropDto>> findByFarmerId(int farmerId) {
        try {
            log.info("Fetching crops for farmer ID {}", farmerId);
            List<CropDto> crops = cropRepository.findByFarmerId(farmerId)
                    .stream()
                    .map(crop -> new CropDto(
                            crop.getFarmerId(),
                            crop.getCropType(),
                            crop.getCropName(),
                            crop.getQuantity(),
                            crop.getPricePerUnit(),
                            crop.getAddress(),
                            crop.getIsAvailable()))
                    .toList();

            return ResponseEntity.ok(crops);
        } catch (Exception e) {
            log.error("Error in findByFarmerId function: {}", e.getMessage());
            throw new CropServiceException("Failed to fetch crops");
        }
    }

    public ResponseEntity<CropDto> findByCropId(int cropId) {
        try {
            log.info("Fetching crop with ID {}", cropId);

            Crop crop = cropRepository.findById(cropId)
                    .orElseThrow(() -> new CropServiceException("Crop not found with ID " + cropId));

            CropDto cropDto = new CropDto(
                    crop.getFarmerId(),
                    crop.getCropType(),
                    crop.getCropName(),
                    crop.getQuantity(),
                    crop.getPricePerUnit(),
                    crop.getAddress(),
                    crop.getIsAvailable()
            );

            return ResponseEntity.ok(cropDto);
        } catch (Exception e) {
            log.error("Error in findByCropId function: {}", e.getMessage());
            throw new CropServiceException("Failed to fetch crop");
        }
    }

}