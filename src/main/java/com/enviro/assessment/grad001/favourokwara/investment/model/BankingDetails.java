package com.enviro.assessment.grad001.favourokwara.investment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Embeddable // can be embeded into an entiity
public class BankingDetails {

    @NotBlank(message = "BANK NAME can not be blank.")
    @NotNull(message = "BANK NAME is mandatory.")
    @Column(name = "bank_name")
    private String bankName;

    @NotBlank(message = "ACCOUNT NUMBER can not be blank.")
    @NotNull(message = "ACCOUNT NUMBER is mandatory.")
    @Column(name = "account_number")
    private String accountNumber;

    public BankingDetails() {
    }
}
