package com.cropdeal.crop.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CropDto {
//    @NotNull(message = "Crop ID is mandatory")
//    @Column(unique = true)  // redundant
//    private int cropId;

    @NotNull(message = "Farmer ID is mandatory")
    private int farmerId;

    @NotBlank(message = "Crop type is mandatory")
    private String cropType; // "vegetable" or "fruit"

    @NotBlank(message = "Crop name is mandatory")
    private String cropName; // "tomato", "apple", etc.

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100000, message = "Quantity cannot exceed 100000")
    @NotNull(message = "Quantity is mandatory")
    private double quantity;

    @Positive(message = "Price must be positive")
    @NotNull(message = "Price is mandatory")
    private double pricePerUnit;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @NotBlank(message = "Availability status is mandatory")
    private String isAvailable;
}
