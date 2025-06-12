package com.cropdeal.user.feign;

import com.cropdeal.user.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dealer")
public interface DealerServiceClient {
    @PostMapping("/api/dealer/create")
    ResponseEntity<String> createDealer(@RequestBody UserDto userDto);

    @DeleteMapping("/api/dealer/remove/{email}")
    public ResponseEntity<String> removeDealer(@PathVariable String email);
}
