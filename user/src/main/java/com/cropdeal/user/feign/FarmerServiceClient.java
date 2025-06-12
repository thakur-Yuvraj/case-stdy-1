package com.cropdeal.user.feign;

import com.cropdeal.user.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "farmer")
public interface FarmerServiceClient {
    @PostMapping("/api/farmer/create")
    ResponseEntity<String> createFarmer(@RequestBody UserDto userDto);

    @DeleteMapping("remove/{email}")
    public ResponseEntity<String> removeFarmer(@PathVariable String email);
}
