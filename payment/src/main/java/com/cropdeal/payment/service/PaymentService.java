package com.cropdeal.payment.service;

import com.cropdeal.payment.dto.PaymentRequestDTO;
import com.cropdeal.payment.exception.PaymentServiceException;
import com.cropdeal.payment.model.PaymentTransaction;
import com.cropdeal.payment.repository.PaymentTransactionRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private static final String STATUS = "status";
    private static final String RECEIPT = "receipt";
    private static final String CURRENCY = "currency";
    private static final String AMOUNT = "amount";

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    private final PaymentTransactionRepository paymentTransactionRepository;

    public ResponseEntity<String> createOrder(PaymentRequestDTO paymentRequest) {
        try {
            log.info("Creating Razorpay order for amount: {}", paymentRequest.getAmount());

            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put(AMOUNT, paymentRequest.getAmount() * 100); // Convert to paise
            orderRequest.put(CURRENCY, paymentRequest.getCurrency());
            orderRequest.put(RECEIPT, generateReceiptId());
            orderRequest.put("payment_capture", 1); // Auto-capture payments

            JSONObject notes = new JSONObject();
            notes.put("dealer_id", paymentRequest.getDealerId());
            notes.put("farmer_id", paymentRequest.getFarmerId());
            orderRequest.put("notes", notes);

            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            log.info("Razorpay order created: {}", razorpayOrder.get("id").toString());

            saveTransactionToDB(paymentRequest, razorpayOrder);

            JSONObject response = new JSONObject();
            response.put("id", razorpayOrder.get("id").toString());
            response.put(AMOUNT, razorpayOrder.get(AMOUNT).toString());
            response.put(CURRENCY, razorpayOrder.get(CURRENCY).toString());
            response.put(RECEIPT, razorpayOrder.get(RECEIPT).toString());
            response.put(STATUS, "created");

            return ResponseEntity.ok(response.toString());

        } catch (RazorpayException e) {
            log.error("Razorpay API error while creating order", e);
            throw new PaymentServiceException("Payment gateway error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while creating payment order", e);
            throw new PaymentServiceException("Internal server error");
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
                log.info("Payment successful for order: {}", razorpayOrderId);
            } else {
                log.warn("No transaction found for order ID: {}", razorpayOrderId);
            }
        } catch (Exception e) {
            log.error("Error updating payment success status", e);
        }
    }

    public void handlePaymentFailure(String razorpayOrderId, String errorCode, String errorDescription) {
        try {
            PaymentTransaction transaction = paymentTransactionRepository.findByRazorpayOrderId(razorpayOrderId);

            if (transaction != null) {
                transaction.setStatus("failed");
                transaction.setUpdatedAt(LocalDateTime.now());

                paymentTransactionRepository.save(transaction);
                log.info("Payment failed for order: {}", razorpayOrderId);
            } else {
                log.warn("No transaction found for failed order ID: {}", razorpayOrderId);
            }
        } catch (Exception e) {
            log.error("Error updating payment failure status", e);
            log.info(errorCode);
            log.info(errorDescription);
        }
    }

    public PaymentTransaction verifyPayment(String razorpayPaymentId) throws RazorpayException {
        try {
            log.info("Verifying payment with ID: {}", razorpayPaymentId);

            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            com.razorpay.Payment payment = razorpayClient.payments.fetch(razorpayPaymentId);

            PaymentTransaction transaction = paymentTransactionRepository.findByRazorpayPaymentId(razorpayPaymentId);

            if (transaction != null) {
                transaction.setStatus("captured".equals(payment.get(STATUS)) ? "completed" : payment.get(STATUS).toString());
                paymentTransactionRepository.save(transaction);
            }

            return transaction;
        } catch (RazorpayException e) {
            log.error("Error verifying payment", e);
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