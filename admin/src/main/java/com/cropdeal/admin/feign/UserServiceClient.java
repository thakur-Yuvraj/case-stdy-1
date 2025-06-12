package com.cropdeal.admin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("user")
public interface UserServiceClient {
    @PostMapping("/api/user/remove/{email}")
    ResponseEntity<String> removeUser(@PathVariable String email);

}
