package com.cropdeal.dealer.dto;


import lombok.Builder;
import lombok.Data;

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