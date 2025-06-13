package com.cropdeal.payment.repository;

import com.cropdeal.payment.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    PaymentTransaction findByRazorpayOrderId(String razorpayOrderId);

    PaymentTransaction findByRazorpayPaymentId(String razorpayPaymentId);
}