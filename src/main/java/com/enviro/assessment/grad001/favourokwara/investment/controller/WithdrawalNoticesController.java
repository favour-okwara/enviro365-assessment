package com.enviro.assessment.grad001.favourokwara.investment.controller;

import org.springframework.web.bind.annotation.RestController;

import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;
import com.enviro.assessment.grad001.favourokwara.investment.service.ProductService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/withdrawal-notices")
public class WithdrawalNoticesController {

    final ProductService productService;

    public WithdrawalNoticesController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<WithdrawalNotice>> getWithdrawalNotice(
        @RequestParam Long productId,
        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate stopDate

    ) {
        if (startDate == null) {
            // default to three months before current date. 
            startDate = LocalDate
                .now()
                .minusDays(LocalDate.now().getDayOfMonth())
                .minusMonths(3);
        }

        if (stopDate == null) {
            stopDate = LocalDate.now();
        }

        List<WithdrawalNotice> notices = productService.getWithdrawalNoticesBetweenDates(productId, startDate, stopDate);
        return ResponseEntity.ok(notices);
    }
}
