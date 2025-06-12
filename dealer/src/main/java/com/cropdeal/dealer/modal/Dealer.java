package com.cropdeal.dealer.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Entity
public class Dealer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Invalid phone number format"
    )
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false)
    private String companyName;

//    @NotBlank(message = "GST number is required")
//    @Pattern(regexp = "^[0-9]{15}$", message = "Invalid GST number format")
    private String gstNumber;

//    @NotBlank(message = "Business address is required")
//    @Column(nullable = false)
    private String businessAddress;
}