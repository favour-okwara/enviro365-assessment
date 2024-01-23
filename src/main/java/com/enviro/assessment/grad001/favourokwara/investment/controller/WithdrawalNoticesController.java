package com.enviro.assessment.grad001.favourokwara.investment.controller;

import org.springframework.web.bind.annotation.RestController;

import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;
import com.enviro.assessment.grad001.favourokwara.investment.service.CSVGeneratorUtil;
import com.enviro.assessment.grad001.favourokwara.investment.service.ProductService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/withdrawal-notices")
public class WithdrawalNoticesController {

    final ProductService productService;
    final CSVGeneratorUtil csvGeneratorUtil;

    public WithdrawalNoticesController(
        ProductService productService,
        CSVGeneratorUtil csvGeneratorUtil
    ) {
        this.productService = productService;
        this.csvGeneratorUtil = csvGeneratorUtil;
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

        // Todo: Overload getWithdrawalNoticesBetweenDates to account for change in dates 
        List<WithdrawalNotice> notices = productService.getWithdrawalNoticesBetweenDates(productId, startDate, stopDate);
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/statement")
    public ResponseEntity<byte[]> getWithdrawalNoticeStatements(
        @RequestParam Long productId,
        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate stopDate
    ) {
        try {
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
    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", 
                String.format("statement-%tF_%tF.csv", startDate, stopDate)
            );
    
            byte[] noticesCSVBytes = csvGeneratorUtil
                .generateCSVFromWithdrawalNotices(notices)
                .getBytes("UTF-8");
            
            return new ResponseEntity<>(noticesCSVBytes, headers, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
}
