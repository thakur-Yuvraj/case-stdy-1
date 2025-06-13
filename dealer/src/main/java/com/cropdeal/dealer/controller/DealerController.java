package com.cropdeal.dealer.controller;


import com.cropdeal.dealer.dto.CropDto;
import com.cropdeal.dealer.dto.UserDto;
import com.cropdeal.dealer.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dealer")
public class DealerController {

    @Autowired
    DealerService dealerService;
    @GetMapping("")
    public ResponseEntity<String> test() {
        return dealerService.test();
    }

    @PostMapping("/create")
    public ResponseEntity<String> addDealer(@RequestBody UserDto userDto) {
        return dealerService.createDealer(userDto);
    }

    @GetMapping("/find/all/crop")
    public ResponseEntity<List<CropDto>> getAllCrop() {
        return dealerService.getAllCrop();
    }

    @GetMapping("/purchase/{cropId}")
    public ResponseEntity<String> purchaseCrop(@PathVariable int cropId) {
        return dealerService.makePurchase(cropId);
    }

    @DeleteMapping("/remove/{email}")
    public ResponseEntity<String> removeDealer(@PathVariable String email) {
        return dealerService.removeDealer(email);
    }

}
