package com.cropdeal.dealer.service;


import com.cropdeal.dealer.dto.CropDto;
import com.cropdeal.dealer.dto.PaymentRequestDTO;
import com.cropdeal.dealer.dto.UserDto;
import com.cropdeal.dealer.feign.CropServiceClient;
import com.cropdeal.dealer.feign.PaymentServiceClient;
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

    @Autowired
    PaymentServiceClient paymentServiceClient;

    public ResponseEntity<String> createDealer(UserDto userDto) {
        log.info("Creating user dealer");
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
            log.error(e.getMessage());
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

    public ResponseEntity<String> makePurchase(int cropId, int dealerId) {

        try {
            CropDto cropDto = cropServiceClient.findByCropId(cropId).getBody();
            if(cropDto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop Not found");
            if(!cropDto.getIsAvailable().equalsIgnoreCase("Yes")) {
                log.info(cropDto.getIsAvailable());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop Not available");
            }
            String farmerId = String.valueOf(cropDto.getFarmerId());
            int totalAmount = (int) (cropDto.getPricePerUnit() * cropDto.getQuantity());
            PaymentRequestDTO paymentRequest = PaymentRequestDTO
                    .builder()
                    .dealerId(String.valueOf(dealerId))
                    .amount(totalAmount) // calculate
                    .farmerId(farmerId)
                    .currency("INR")
                    .build();
            ResponseEntity<String> paymentResponse = paymentServiceClient.createOrder(paymentRequest);
            if(!paymentResponse.getStatusCode().is2xxSuccessful())
                return paymentResponse;
            cropServiceClient.disableCrop(cropId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Purchase of crop was Successfully");
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


