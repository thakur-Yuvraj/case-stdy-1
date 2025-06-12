package com.cropdeal.payment.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Data
@Builder
public class PaymentRequestDTO {
    private String dealerId;
    private String farmerId;
    private int amount;
    private String currency;

    // Constructors, getters, and setters
    public PaymentRequestDTO() {}

    public PaymentRequestDTO(String dealerId, String farmerId, int amount, String currency) {
        this.dealerId = dealerId;
        this.farmerId = farmerId;
        this.amount = amount;
        this.currency = currency;
    }
}