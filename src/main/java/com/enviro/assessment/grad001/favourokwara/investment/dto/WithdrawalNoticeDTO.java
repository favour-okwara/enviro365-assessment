package com.enviro.assessment.grad001.favourokwara.investment.dto;

import java.time.LocalDate;

import com.enviro.assessment.grad001.favourokwara.investment.model.BankingDetails;
import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WithdrawalNoticeDTO {

    @NotNull(message = "WITHDRAWAL AMOUNT is mandatory.")
    private Double amount;

    @NotBlank(message = "BANK NAME can not be blank.")
    @NotNull(message = "BANK NAME is mandatory.")
    private String bankName;

    @NotBlank(message = "ACCOUNT NUMBER can not be blank.")
    @NotNull(message = "ACCOUNT NUMBER is mandatory.")
    private String accountNumber;

    @NotNull(message = "WITHDRAWAL DATE is mandatory.")
    private LocalDate withdrawalDate;

    public WithdrawalNotice toWithdrawalNotice() {
        return new WithdrawalNotice(amount, withdrawalDate, new BankingDetails(bankName, accountNumber));
    }
}
