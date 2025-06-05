package com.cropdeal.crop.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

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
    @jakarta.validation.constraints.NotNull(message = "Quantity is mandatory")
    private double quantity;

    @Positive(message = "Price must be positive")
    @NotNull(message = "Price is mandatory")
    private double pricePerUnit;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @NotBlank(message = "Availability status is mandatory")
    private String isAvailable;
}