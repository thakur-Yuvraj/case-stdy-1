package com.cropdeal.user.controller;


import com.cropdeal.user.dto.CropDto;
import com.cropdeal.user.service.DealerService;
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

    @GetMapping("/find/all/crop")
    public ResponseEntity<List<CropDto>> getAllCrop() {
        return dealerService.getAllCrop();
    }

    @GetMapping("/purchase/{cropId}")
    public ResponseEntity<String> purchaseCrop(@PathVariable int cropId) {
        return dealerService.makePurchase(cropId);
    }

}
