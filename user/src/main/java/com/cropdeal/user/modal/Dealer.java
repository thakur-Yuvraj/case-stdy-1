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
@DiscriminatorValue("DEALER")
public class Dealer extends User {
    private String companyName;
    private String gstNumber;
    private String businessAddress;
}