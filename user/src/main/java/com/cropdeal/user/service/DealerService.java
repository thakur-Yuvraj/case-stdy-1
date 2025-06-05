package com.cropdeal.user.service;


import com.cropdeal.user.dto.CropDto;
import com.cropdeal.user.feign.CropServiceClient;
import com.cropdeal.user.repository.UserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealerService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CropServiceClient cropServiceClient;

    public ResponseEntity<String> test() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body("Hello from dealer service");
        }catch (Exception e) {
            String msg = "Err in dealer service -> test function";
            log.info(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg + e.getMessage());
        }

    }

    public ResponseEntity<List<CropDto>> getAllCrop() {
        try {
            ResponseEntity<List<CropDto>> response = cropServiceClient.findAllCrop();
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).build();
            }
        } catch (FeignException e) {
            log.error("Error fetching crops: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<String> makePurchase(int cropId) {

        try {
            CropDto cropDto = cropServiceClient.findByCropId(cropId).getBody();
            if(cropDto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop Not found");
            if(cropDto.getIsAvailable().equalsIgnoreCase("No")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop Not available");
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Purchase of crop Successfully");
        }
        catch (Exception e) {
            String msg = "Err in DealerService -> makePurchase function ";
            log.info(msg);
            log.error(msg + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg + e.getMessage());
        }


    }
}
