package com.cropdeal.user.modal;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@DiscriminatorValue("FARMER")
@AllArgsConstructor
@NoArgsConstructor
public class Farmer extends User {
    private String bankAccountNumber;
    private String ifscCode;

    @Override
    public String toString(){
        return super.toString() + "bankAccNo" + bankAccountNumber + "IFSC" + ifscCode;
    }
}