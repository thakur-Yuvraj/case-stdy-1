package com.cropdeal.crop.service;

import com.cropdeal.crop.dto.CropDto;
import com.cropdeal.crop.model.Crop;
import com.cropdeal.crop.repository.CropRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CropService {

    @Autowired
    CropRepository cropRepository;

    public ResponseEntity<String> homePage() {
        try {
            return ResponseEntity.ok("This is homepage");
        } catch (Exception e) {
            log.info("Err in corp service -> homePage function");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Getting error in crop service -> homePage " + e.getMessage());
        }
    }

    public ResponseEntity<String> addCrop(CropDto cropDto) {
        try {
            // Convert DTO to Entity
            Crop crop = new Crop();
//            crop.setCropId(cropDto.getCropId());
            crop.setFarmerId(cropDto.getFarmerId());
            crop.setCropType(cropDto.getCropType());
            crop.setCropName(cropDto.getCropName());
            crop.setQuantity(cropDto.getQuantity());
            crop.setPricePerUnit(cropDto.getPricePerUnit());
            crop.setAddress(cropDto.getAddress());
            crop.setIsAvailable(cropDto.getIsAvailable());
            if(cropRepository.existsById(crop.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Crop by this id ="+ crop.getId() + " exist in the database");
            }
            cropRepository.save(crop);
            return ResponseEntity.ok("Crop Added successfully");
        } catch (Exception e) {
            log.info("Err in crop service -> addCrop function");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Err in crop Service -> addCrop " + e.getMessage());
        }
    }

    public ResponseEntity<String> removeCropByIdAndFarmerId(int cropId, int farmerId) {
        try {
            if(!cropRepository.existsByIdAndFarmerId(cropId, farmerId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("crop not found by crop id");
            }
            cropRepository.deleteById(cropId);
            return ResponseEntity.ok("Crop with ID " + cropId + " deleted successfully");
        } catch (Exception e) {
            log.info("Err in crop Service -> removeCropById function");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in crop service remove Crop by id " + e.getMessage());
        }
    }

    public ResponseEntity<List<CropDto>> findAllCrop() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(cropRepository.findAll()
                    .stream()
                    .map(crop -> new CropDto(crop.getFarmerId(), crop.getCropType(), crop.getCropName(), crop.getQuantity(), crop.getPricePerUnit(), crop.getAddress(), crop.getIsAvailable()))
                    .toList()
            );
        } catch (Exception e) {
            log.info("Error in crop service -> findAllCrop functon not working properly");
            System.out.println("Error in crop service -> findAllCrop functon not working properly");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    public ResponseEntity<List<CropDto>> findByName(String name) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND).body(cropRepository.findByCropNameIgnoreCase(name)
                    .stream()
                    .map(crop -> new CropDto(crop.getFarmerId(), crop.getCropType(), crop.getCropName(), crop.getQuantity(), crop.getPricePerUnit(), crop.getAddress(), crop.getIsAvailable()))
                    .toList()
            );
        } catch (Exception e) {
            log.info("Error in crop service -> findByName functon not working properly");
            System.out.println("Error in crop service -> findAllCrop functon not working properly");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<List<CropDto>> findByFarmerId(int farmerId) {
        try {
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
            log.info("Error in crop service -> findByFarmerId function", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<CropDto> findByCropId(int cropId) {
        if(cropRepository.existsById(cropId)) {
            Crop crop = cropRepository.findById(cropId).get();

            CropDto cropDto = new CropDto(crop.getFarmerId(), crop.getCropType(), crop.getCropName(), crop.getQuantity(), crop.getPricePerUnit(), crop.getAddress(), crop.getIsAvailable());
            crop.setIsAvailable("No");
            cropRepository.save(crop);
            return ResponseEntity.status(HttpStatus.OK).body(cropDto);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}


