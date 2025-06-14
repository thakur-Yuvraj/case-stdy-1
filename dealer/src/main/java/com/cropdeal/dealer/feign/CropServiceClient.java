package com.cropdeal.dealer.feign;


import com.cropdeal.dealer.dto.CropDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "crop")
public interface CropServiceClient {
    @PostMapping("/api/crop/add")
    ResponseEntity<String> addCrop(@RequestBody CropDto cropDto);

    @GetMapping("/api/crop/find/all/{farmerId}")
    ResponseEntity<List<CropDto>> findByFarmerId(@PathVariable int farmerId);

    @GetMapping("/api/crop/find/all")
    ResponseEntity<List<CropDto>> findAllCrop();


    @GetMapping("/api/crop/find/{cropId}")
    ResponseEntity<CropDto> findByCropId(@PathVariable int cropId);

    @PostMapping("/api/crop/makeCropUnavailable/{cropId}")
    ResponseEntity<String> disableCrop(@PathVariable int cropId);

}
