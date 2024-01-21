package com.enviro.assessment.grad001.favourokwara.investment.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "withdrawal_notices")
public class WithdrawalNotice extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "WITHDRAWAL AMOUNT is mandatory.")
    @Column(name = "amount")
    private Double amount;

    @NotBlank(message = "BANK NAME can not be blank.")
    @NotNull(message = "BANK NAME is mandatory.")
    @Column(name = "bank_name")
    private String bankName;

    @NotBlank(message = "ACCOUNT NUMBER can not be blank.")
    @NotNull(message = "ACCOUNT NUMBER is mandatory.")
    @Column(name = "account_number")
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WithdrawalStatus status;

    @NotNull(message = "WITHDRAWAL DATE is mandatory.")
    @Column(name = "withdrawal_date")
    private LocalDateTime withdrawalDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public WithdrawalNotice(
        Double amount,
        String bankName,
        String accountNumber,
        LocalDateTime withdrawalDate
    ) {
        this();
        this.amount = amount;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.withdrawalDate = withdrawalDate;
    }

    public WithdrawalNotice() {
        this.status = WithdrawalStatus.PENDING;
    }
}
