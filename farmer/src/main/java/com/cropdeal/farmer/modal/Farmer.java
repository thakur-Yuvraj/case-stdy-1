package com.cropdeal.farmer.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
//@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("FARMER")
@AllArgsConstructor
@NoArgsConstructor
public class Farmer {
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
            regexp = "^\\+?\\d{10,15}$",
            message = "Invalid phone number format"
    )
    @Column(nullable = false)
    private String phone;

//    @NotBlank(message = "Bank account number is required")
//    @Pattern(regexp = "^[0-9]{9,18}$", message = "Invalid bank account number")
    private String bankAccountNumber;

//    @NotBlank(message = "IFSC code is required")
//    @Pattern(regexp = "^[A-Za-z]{4}[0-9]{7}$", message = "Invalid IFSC code format")
    private String ifscCode;
}
