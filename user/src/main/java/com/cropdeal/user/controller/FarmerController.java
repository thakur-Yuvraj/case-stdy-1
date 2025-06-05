package com.cropdeal.user.controller;


import com.cropdeal.user.dto.CropDto;
import com.cropdeal.user.service.FarmerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farmer")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;

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
}
