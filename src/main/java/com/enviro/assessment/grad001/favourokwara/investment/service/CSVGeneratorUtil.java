package com.enviro.assessment.grad001.favourokwara.investment.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;

@Component
public class CSVGeneratorUtil {

    public String generateCSVFromWithdrawalNotices(
        List<WithdrawalNotice> withdrawalNotices) {
        final String header = "Id,Status,Amount,WithdrawalDate,BankName,AccountNumber,CreatedDate\n";

        StringBuilder content = new StringBuilder();
        content.append(header);

        for (WithdrawalNotice withdrawalNotice : withdrawalNotices) {
            content
                .append(withdrawalNotice.getId()).append(",")
                .append(withdrawalNotice.getStatus()).append(",")
                .append(withdrawalNotice.getAmount()).append(",")
                .append(withdrawalNotice.getWithdrawalDate()).append(",")
                .append(withdrawalNotice.getBankingDetails().getBankName()).append(",")
                .append(withdrawalNotice.getBankingDetails().getAccountNumber()).append(",")
                .append(withdrawalNotice.getCreatedDate()).append("\n");
        }

        return content.toString();
    }
}
