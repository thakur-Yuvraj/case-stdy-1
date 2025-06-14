package com.cropdeal.admin.modal;


import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@Entity
public class Admin {
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

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Admin level is required")
    @Pattern(
            regexp = "^(SUPER_ADMIN|REGIONAL_ADMIN|SUB_ADMIN|TEMPORARY_ADMIN)$",
            message = "Invalid admin level"
    )
    private String adminLevel;

    private boolean canManageUsers;
    private boolean canManageCrops;
    private boolean canManageSystemSettings;
}
