package com.cropdeal.api.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="auth")
public interface AuthServiceClient {
}
