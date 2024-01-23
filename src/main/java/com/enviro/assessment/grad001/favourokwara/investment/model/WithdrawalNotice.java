package com.enviro.assessment.grad001.favourokwara.investment.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "withdrawal_notices")
public class WithdrawalNotice extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "WITHDRAWAL AMOUNT is mandatory.")
    @NonNull
    @Column(name = "amount")
    private Double amount;
  
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WithdrawalStatus status = WithdrawalStatus.PENDING;

    @NotNull(message = "WITHDRAWAL DATE is mandatory.")
    @NonNull
    @Column(name = "withdrawal_date")
    private LocalDate withdrawalDate;

    @Embedded
    @NonNull
    private BankingDetails bankingDetails;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public WithdrawalNotice() {
    }
}
