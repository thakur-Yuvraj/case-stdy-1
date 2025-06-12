package com.cropdeal.dealer.service;


import com.cropdeal.dealer.dto.CropDto;
import com.cropdeal.dealer.dto.UserDto;
import com.cropdeal.dealer.feign.CropServiceClient;
import com.cropdeal.dealer.modal.Dealer;
import com.cropdeal.dealer.repository.DealerRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealerService {
    @Autowired
    CropServiceClient cropServiceClient;

    @Autowired
    private DealerRepository dealerRepository;

    public ResponseEntity<String> createDealer(UserDto userDto) {
        try {
            Dealer dealer = Dealer.builder()
                    .email(userDto.getEmail())
                    .fullName(userDto.getFullName())
                    .phone(userDto.getPhone())
                    .businessAddress(userDto.getAddress())
                    .companyName(null)
                    .gstNumber(null)
                    .build();

            dealerRepository.save(dealer);
            return ResponseEntity.ok("Dealer registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Dealer creation failed");
        }
    }

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
            log.error("{}{}", msg, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg + e.getMessage());
        }
    }
    public ResponseEntity<String> removeDealer(String email) {
        if(!dealerRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("farmer not found");
        }
        dealerRepository.deleteByEmail(email);
        return ResponseEntity.ok("farmer removed");
    }
}


