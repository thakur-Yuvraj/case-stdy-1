package com.cropdeal.crop.controller;

import com.cropdeal.crop.dto.CropDto;
import com.cropdeal.crop.model.Crop;
import com.cropdeal.crop.service.CropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("crop")
public class Controller {

    @Autowired
    CropService cropService;

    @GetMapping("")
    public ResponseEntity<String> home() {
        return cropService.homePage();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCrop(@RequestBody CropDto cropDto) {
        return cropService.addCrop(cropDto);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeCropByIdAndFarmerId(@PathVariable int id, @RequestHeader("X-User-ID") Integer userId) {
        return cropService.removeCropByIdAndFarmerId(id, userId);
    }

    @GetMapping("/find/all")
    public ResponseEntity<List<CropDto>> findAllCrop() {
        return cropService.findAllCrop();
    }

    @GetMapping("/find/all/{farmerId}")
    public ResponseEntity<List<CropDto>> findByFarmerId(@PathVariable int farmerId) {
        return cropService.findByFarmerId(farmerId);
    }

    @GetMapping("/find/{cropId}")
    public ResponseEntity<CropDto> findByCropId(@PathVariable int cropId) {
        return cropService.findByCropId(cropId);
    }

}
