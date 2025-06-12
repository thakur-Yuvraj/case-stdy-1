package com.cropdeal.user.feign;

import com.cropdeal.user.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "admin")
public interface AdminServiceClient {
    @PostMapping("/api/admin/create")
    ResponseEntity<String> createAdmin(@RequestBody UserDto userDto);
}
