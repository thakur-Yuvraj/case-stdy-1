package com.cropdeal.payment.controller;

import com.cropdeal.payment.dto.PaymentRequestDTO;
import com.cropdeal.payment.exception.PaymentServiceException;
import com.cropdeal.payment.model.PaymentTransaction;
import com.cropdeal.payment.service.PaymentService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody PaymentRequestDTO paymentRequest) {
        try {
            log.info("Creating payment order for dealer ID: {}", paymentRequest.getDealerId());
            return paymentService.createOrder(paymentRequest);
        } catch (Exception e) {
            log.error("Error in createOrder endpoint: {}", e.getMessage());
            throw new PaymentServiceException("Failed to create payment order");
        }
    }

    @PostMapping("/payment-success")
    public ResponseEntity<String> paymentSuccess(
            @RequestParam("razorpay_order_id") String razorpayOrderId,
            @RequestParam("razorpay_payment_id") String razorpayPaymentId) {
        try {
            log.info("Handling payment success for order ID: {}", razorpayPaymentId);
            paymentService.handlePaymentSuccess(razorpayOrderId, razorpayPaymentId);
            return ResponseEntity.ok("Payment status updated to success");
        } catch (Exception e) {
            log.error("Error in paymentSuccess endpoint: {}", e.getMessage());
            throw new PaymentServiceException("Failed to update payment success status");
        }
    }

    @PostMapping("/payment-failure")
    public ResponseEntity<String> paymentFailure(
            @RequestParam("razorpay_order_id") String razorpayOrderId,
            @RequestParam("error_code") String errorCode,
            @RequestParam("error_description") String errorDescription) {
        try {
            log.info("Handling payment failure for order ID: {}", razorpayOrderId);
            paymentService.handlePaymentFailure(razorpayOrderId, errorCode, errorDescription);
            return ResponseEntity.ok("Payment status updated to failed");
        } catch (Exception e) {
            log.error("Error in paymentFailure endpoint: {}", e.getMessage());
            throw new PaymentServiceException("Failed to update payment failure status");
        }
    }

    @GetMapping("/verify-payment/{paymentId}")
    public ResponseEntity<PaymentTransaction> verifyPayment(@PathVariable String paymentId) throws RazorpayException {
        try {
            log.info("Verifying payment with ID: {}", paymentId);
            return ResponseEntity.ok(paymentService.verifyPayment(paymentId));
        } catch (Exception e) {
            log.error("Error in verifyPayment endpoint: {}", e.getMessage());
            throw new PaymentServiceException("Failed to verify payment");
        }
    }
}