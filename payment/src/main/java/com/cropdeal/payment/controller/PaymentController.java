package com.cropdeal.payment.controller;

import com.cropdeal.payment.dto.PaymentRequestDTO;
import com.cropdeal.payment.model.PaymentTransaction;
import com.cropdeal.payment.service.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody PaymentRequestDTO paymentRequest) {
        return paymentService.createOrder(paymentRequest);
    }

    @PostMapping("/payment-success")
    public ResponseEntity<String> paymentSuccess(
            @RequestParam String razorpay_order_id,
            @RequestParam String razorpay_payment_id) {

        paymentService.handlePaymentSuccess(razorpay_order_id, razorpay_payment_id);
        return ResponseEntity.ok("Payment status updated to success");
    }

    @PostMapping("/payment-failure")
    public ResponseEntity<String> paymentFailure(
            @RequestParam String razorpay_order_id,
            @RequestParam String error_code,
            @RequestParam String error_description) {

        paymentService.handlePaymentFailure(razorpay_order_id, error_code, error_description);
        return ResponseEntity.ok("Payment status updated to failed");
    }

    @GetMapping("/verify-payment/{paymentId}")
    public ResponseEntity<PaymentTransaction> verifyPayment(
            @PathVariable String paymentId) throws RazorpayException {

        return ResponseEntity.ok(paymentService.verifyPayment(paymentId));
    }
}