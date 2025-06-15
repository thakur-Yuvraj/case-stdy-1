package com.cropdeal.farmer.feign;


import com.cropdeal.farmer.dto.CropDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@FeignClient(name = "crop")
public interface CropServiceClient {
    @PostMapping("/api/crop/add")
    ResponseEntity<String> addCrop(@RequestBody CropDto cropDto);
    @DeleteMapping("/api/crop/remove/{id}")
    ResponseEntity<String> removeCropByIdAndFarmerId(@PathVariable int id, @RequestHeader("X-User-ID") Integer userId);

    @GetMapping("/api/crop/find/all/{farmerId}")
    ResponseEntity<List<CropDto>> findByFarmerId(@PathVariable int farmerId);

    @GetMapping("/api/crop/find/all")
    ResponseEntity<List<CropDto>> findAllCrop();


    @GetMapping("/api/crop/find/{cropId}")
    ResponseEntity<CropDto> findByCropId(@PathVariable int cropId);

    @DeleteMapping("/api/crop/remove/id/{id}")
    public ResponseEntity<String> removeCropById(@PathVariable int id);

}
