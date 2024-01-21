package com.enviro.assessment.grad001.favourokwara.investment.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class WithdrawalNoticeDTO {

    @NotNull(message = "PRODUCT ID is mandatory.")
    private Long productId;

    @NotNull(message = "WITHDRAWAL AMOUNT is mandatory.")
    private Double amount;

    @NotBlank(message = "BANK NAME can not be blank.")
    @NotNull(message = "BANK NAME is mandatory.")
    private String bankName;

    @NotBlank(message = "ACCOUNT NUMBER can not be blank.")
    @NotNull(message = "ACCOUNT NUMBER is mandatory.")
    private String accountNumber;

    @NotNull(message = "WITHDRAWAL DATE is mandatory.")
    private LocalDateTime withdrawalDate;
}
