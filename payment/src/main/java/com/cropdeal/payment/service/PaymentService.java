package com.cropdeal.payment.service;

import com.cropdeal.payment.dto.PaymentRequestDTO;
import com.cropdeal.payment.model.PaymentTransaction;
import com.cropdeal.payment.repository.PaymentTransactionRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    public ResponseEntity<String> createOrder(PaymentRequestDTO paymentRequest) {
        try {
            // Initialize Razorpay client
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);

            // Create order request
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", paymentRequest.getAmount() * 100); // Convert to paise
            orderRequest.put("currency", paymentRequest.getCurrency());
            orderRequest.put("receipt", generateReceiptId());
            orderRequest.put("payment_capture", 1); // Auto-capture payments

            // Add notes for tracking
            JSONObject notes = new JSONObject();
            notes.put("dealer_id", paymentRequest.getDealerId());
            notes.put("farmer_id", paymentRequest.getFarmerId());
            orderRequest.put("notes", notes);

            logger.info("Creating Razorpay order for amount: {}", paymentRequest.getAmount());

            // Create the order on Razorpay
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            logger.info("Razorpay order created: {}", razorpayOrder.get("id").toString());

            // Save transaction to database
            PaymentTransaction transaction = saveTransactionToDB(paymentRequest, razorpayOrder);

            // Prepare response for frontend
            JSONObject response = new JSONObject();
            response.put("id", razorpayOrder.get("id").toString());
            response.put("amount", razorpayOrder.get("amount").toString());
            response.put("currency", razorpayOrder.get("currency").toString());
            response.put("receipt", razorpayOrder.get("receipt").toString());
            response.put("status", "created");

            return ResponseEntity.ok(response.toString());

        } catch (RazorpayException e) {
            logger.error("Razorpay API error while creating order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Payment gateway error: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            logger.error("Unexpected error while creating payment order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Internal server error\"}");
        }
    }

    public void handlePaymentSuccess(String razorpayOrderId, String razorpayPaymentId) {
        try {
            PaymentTransaction transaction = paymentTransactionRepository.findByRazorpayOrderId(razorpayOrderId);

            if (transaction != null) {
                transaction.setRazorpayPaymentId(razorpayPaymentId);
                transaction.setStatus("success");
                transaction.setUpdatedAt(LocalDateTime.now());

                paymentTransactionRepository.save(transaction);
                logger.info("Payment successful for order: {}", razorpayOrderId);
            } else {
                logger.warn("No transaction found for order ID: {}", razorpayOrderId);
            }
        } catch (Exception e) {
            logger.error("Error updating payment success status", e);
        }
    }

    public void handlePaymentFailure(String razorpayOrderId, String errorCode, String errorDescription) {
        try {
            PaymentTransaction transaction = paymentTransactionRepository.findByRazorpayOrderId(razorpayOrderId);

            if (transaction != null) {
                transaction.setStatus("failed");
//                transaction.setErrorCode(errorCode);
//                transaction.setErrorMessage(errorDescription);
                transaction.setUpdatedAt(LocalDateTime.now());

                paymentTransactionRepository.save(transaction);
                logger.info("Payment failed for order: {}", razorpayOrderId);
            } else {
                logger.warn("No transaction found for failed order ID: {}", razorpayOrderId);
            }
        } catch (Exception e) {
            logger.error("Error updating payment failure status", e);
        }
    }

    public PaymentTransaction verifyPayment(String razorpayPaymentId) throws RazorpayException {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            com.razorpay.Payment payment = razorpayClient.payments.fetch(razorpayPaymentId);

            PaymentTransaction transaction = paymentTransactionRepository.findByRazorpayPaymentId(razorpayPaymentId);

            if (transaction != null) {
                if ("captured".equals(payment.get("status"))) {
                    transaction.setStatus("completed");
                } else {
                    transaction.setStatus(payment.get("status").toString());
                }
                paymentTransactionRepository.save(transaction);
            }

            return transaction;
        } catch (RazorpayException e) {
            logger.error("Error verifying payment", e);
            throw e;
        }
    }

    private PaymentTransaction saveTransactionToDB(PaymentRequestDTO paymentRequest, com.razorpay.Order razorpayOrder) {
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setDealerId(paymentRequest.getDealerId());
        transaction.setFarmerId(paymentRequest.getFarmerId());
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setCurrency(paymentRequest.getCurrency());
        transaction.setRazorpayOrderId(razorpayOrder.get("id").toString());
        transaction.setStatus("created");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        return paymentTransactionRepository.save(transaction);
    }

    private String generateReceiptId() {
        return "rcpt_" + System.currentTimeMillis();
    }
}