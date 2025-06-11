package com.cropdeal.auth.feign;

import com.cropdeal.auth.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user")
public interface UserServiceClient {
    @PostMapping("/api/user/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto);

}
