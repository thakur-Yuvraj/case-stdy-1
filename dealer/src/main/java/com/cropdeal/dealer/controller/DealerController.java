package com.cropdeal.dealer.controller;

import com.cropdeal.dealer.dto.CropDto;
import com.cropdeal.dealer.dto.UserDto;
import com.cropdeal.dealer.exception.DealerServiceException;
import com.cropdeal.dealer.service.DealerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dealer")
@RequiredArgsConstructor
@Slf4j
public class DealerController {

    private final DealerService dealerService;

    @GetMapping("")
    public ResponseEntity<String> test() {
        try {
            log.info("Testing dealer service endpoint");
            return dealerService.test();
        } catch (Exception e) {
            log.error("Error in test endpoint: {}", e.getMessage());
            throw new DealerServiceException("Internal Server Error");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> addDealer(@RequestBody UserDto userDto) {
        try {
            log.info("Creating dealer with email: {}", userDto.getEmail());
            return dealerService.createDealer(userDto);
        } catch (Exception e) {
            log.error("Error in createDealer endpoint: {}", e.getMessage());
            throw new DealerServiceException("Failed to create dealer");
        }
    }

    @GetMapping("/find/all/crop")
    public ResponseEntity<List<CropDto>> getAllCrop() {
        try {
            log.info("Fetching all crops");
            return dealerService.getAllCrop();
        } catch (Exception e) {
            log.error("Error in getAllCrop endpoint: {}", e.getMessage());
            throw new DealerServiceException("Failed to fetch crops");
        }
    }

    @PostMapping("/purchase/{cropId}")
    public ResponseEntity<String> purchaseCrop(@PathVariable int cropId,
                                               @RequestHeader("X-User-ID") Integer userId) {
        try {
            log.info("Processing purchase request for crop ID {} by dealer ID {}", cropId, userId);
            return dealerService.makePurchase(cropId, userId);
        } catch (Exception e) {
            log.error("Error in purchaseCrop endpoint: {}", e.getMessage());
            throw new DealerServiceException("Failed to complete purchase");
        }
    }

    @DeleteMapping("/remove/{email}")
    public ResponseEntity<String> removeDealer(@PathVariable String email) {
        try {
            log.info("Removing dealer with email: {}", email);
            return dealerService.removeDealer(email);
        } catch (Exception e) {
            log.error("Error in removeDealer endpoint: {}", e.getMessage());
            throw new DealerServiceException("Failed to remove dealer");
        }
    }
}