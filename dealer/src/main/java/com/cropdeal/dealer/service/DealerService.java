package com.cropdeal.dealer.service;

import com.cropdeal.dealer.dto.CropDto;
import com.cropdeal.dealer.dto.PaymentRequestDTO;
import com.cropdeal.dealer.dto.UserDto;
import com.cropdeal.dealer.exception.DealerServiceException;
import com.cropdeal.dealer.feign.CropServiceClient;
import com.cropdeal.dealer.feign.PaymentServiceClient;
import com.cropdeal.dealer.modal.Dealer;
import com.cropdeal.dealer.repository.DealerRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealerService {
    private final CropServiceClient cropServiceClient;
    private final DealerRepository dealerRepository;
    private final PaymentServiceClient paymentServiceClient;

    public ResponseEntity<String> createDealer(UserDto userDto) {
        try {
            log.info("Creating dealer with email: {}", userDto.getEmail());

            Dealer dealer = Dealer.builder()
                    .email(userDto.getEmail())
                    .fullName(userDto.getFullName())
                    .phone(userDto.getPhone())
                    .businessAddress(userDto.getAddress())
                    .companyName(null)
                    .gstNumber(null)
                    .build();

            dealerRepository.save(dealer);
            log.info("Dealer {} registered successfully", dealer.getEmail());

            return ResponseEntity.ok("Dealer registered successfully");
        } catch (Exception e) {
            log.error("Dealer creation failed: {}", e.getMessage());
            throw new DealerServiceException("Failed to create dealer");
        }
    }

    public ResponseEntity<String> test() {
        try {
            log.info("Testing dealer service endpoint");
            return ResponseEntity.ok("Hello from dealer service");
        } catch (Exception e) {
            log.error("Error in dealer service test endpoint: {}", e.getMessage());
            throw new DealerServiceException("Internal Server Error");
        }
    }

    public ResponseEntity<List<CropDto>> getAllCrop() {
        try {
            log.info("Fetching all crops");
            ResponseEntity<List<CropDto>> response = cropServiceClient.findAllCrop();

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                log.warn("Failed to fetch crops, response status: {}", response.getStatusCode());
                return ResponseEntity.status(response.getStatusCode()).build();
            }
        } catch (FeignException e) {
            log.error("Error fetching crops: {}", e.getMessage());
            throw new DealerServiceException("Failed to fetch crops");
        }
    }

    public ResponseEntity<String> makePurchase(int cropId, int dealerId) {
        try {
            log.info("Processing purchase request for crop ID: {}", cropId);

            CropDto cropDto = cropServiceClient.findByCropId(cropId).getBody();
            if (cropDto == null) {
                log.warn("Crop {} not found", cropId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop Not found");
            }

            if (!cropDto.getIsAvailable().equalsIgnoreCase("Yes")) {
                log.warn("Crop {} is not available", cropId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Crop Not available");
            }

            int totalAmount = (int) (cropDto.getPricePerUnit() * cropDto.getQuantity());
            PaymentRequestDTO paymentRequest = PaymentRequestDTO.builder()
                    .dealerId(String.valueOf(dealerId))
                    .amount(totalAmount)
                    .farmerId(String.valueOf(cropDto.getFarmerId()))
                    .currency("INR")
                    .build();

            ResponseEntity<String> paymentResponse = paymentServiceClient.createOrder(paymentRequest);
            if (!paymentResponse.getStatusCode().is2xxSuccessful()) {
                log.error("Payment processing failed");
                return paymentResponse;
            }

            cropServiceClient.disableCrop(cropId);
            log.info("Purchase of crop {} completed successfully", cropId);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Purchase of crop was successful");
        } catch (Exception e) {
            log.error("Error processing purchase request: {}", e.getMessage());
            throw new DealerServiceException("Purchase transaction failed");
        }
    }

    public ResponseEntity<String> removeDealer(String email) {
        try {
            log.info("Removing dealer with email: {}", email);

            if (!dealerRepository.existsByEmail(email)) {
                log.warn("Dealer {} not found", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dealer not found");
            }

            dealerRepository.deleteByEmail(email);
            log.info("Dealer {} removed successfully", email);

            return ResponseEntity.ok("Dealer removed");
        } catch (Exception e) {
            log.error("Error removing dealer: {}", e.getMessage());
            throw new DealerServiceException("Failed to remove dealer");
        }
    }
}