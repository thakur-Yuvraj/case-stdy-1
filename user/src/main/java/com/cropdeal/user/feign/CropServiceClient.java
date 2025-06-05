package com.cropdeal.user.feign;

import com.cropdeal.user.dto.CropDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "crop")
public interface CropServiceClient {

//    @GetMapping("/crop/{id}")
//    Crop getCropById(@PathVariable Integer id);

//    @PostMapping("/crop/")
//    Crop createCrop(@RequestBody CropDto cropDto);
    @PostMapping("/crop/add")
    ResponseEntity<String> addCrop(@RequestBody CropDto cropDto);
    @DeleteMapping("/crop/remove/{id}")
    ResponseEntity<String> removeCropByIdAndFarmerId(@PathVariable int id, @RequestHeader("X-User-ID") Integer userId);

    @GetMapping("/crop/find/all/{farmerId}")
    ResponseEntity<List<CropDto>> findByFarmerId(@PathVariable int farmerId);

    @GetMapping("/crop/find/all")
    ResponseEntity<List<CropDto>> findAllCrop();



    @GetMapping("/crop/find/{cropId}")
    ResponseEntity<CropDto> findByCropId(@PathVariable int cropId);

}
