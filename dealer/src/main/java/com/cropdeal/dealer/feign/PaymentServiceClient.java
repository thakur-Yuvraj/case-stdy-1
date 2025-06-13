package com.cropdeal.dealer.feign;

import com.cropdeal.dealer.dto.PaymentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment")
public interface PaymentServiceClient {

    @PostMapping("/api/payment/create-order")
    ResponseEntity<String> createOrder(@RequestBody PaymentRequestDTO paymentRequest);

}
