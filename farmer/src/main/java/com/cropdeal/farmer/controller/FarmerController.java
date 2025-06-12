package com.cropdeal.farmer.controller;


import com.cropdeal.farmer.dto.CropDto;
import com.cropdeal.farmer.service.FarmerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farmer")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

    @GetMapping("")
    public ResponseEntity<String> home() {
        return farmerService.home();
    }

    @PostMapping("/crop")
    public ResponseEntity<String> addCrop(@RequestBody CropDto cropDto,
                                        @RequestHeader("X-User-ID") Integer userId) {
        return farmerService.addCrop(userId, cropDto);
    }

    @DeleteMapping("/crop/{cropId}")
    public ResponseEntity<String> removeCrop(@PathVariable Integer cropId,
                                             @RequestHeader("X-User-ID") Integer userId) {

        return farmerService.removeCrop(userId, cropId);
    }

    @GetMapping("/crop/profile/{userId}")
    public ResponseEntity<String> getProfile(@PathVariable int userId) {
        return farmerService.getProfile(userId);
    }

    @DeleteMapping("remove/{email}")
    public ResponseEntity<String> removeFarmer(@PathVariable String email) {
        return farmerService.removeFarmer(email);
    }
}
