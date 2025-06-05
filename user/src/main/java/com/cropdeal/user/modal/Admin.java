package com.cropdeal.user.modal;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    // Admin-specific fields
    private String department;
    private String adminLevel; // e.g., "SUPER_ADMIN", "REGIONAL_ADMIN", "SUB_ADMIN, "TEMPORARY_ADMIN"
    private boolean canManageUsers;
    private boolean canManageCrops;
    private boolean canManageSystemSettings;

}
