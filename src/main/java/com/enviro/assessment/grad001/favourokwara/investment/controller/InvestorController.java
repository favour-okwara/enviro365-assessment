package com.enviro.assessment.grad001.favourokwara.investment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enviro.assessment.grad001.favourokwara.investment.dto.WithdrawalNoticeDTO;
import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;
import com.enviro.assessment.grad001.favourokwara.investment.service.CSVGeneratorUtil;
import com.enviro.assessment.grad001.favourokwara.investment.service.InvestorService;
import com.enviro.assessment.grad001.favourokwara.investment.service.ProductService;
import com.enviro.assessment.grad001.favourokwara.investment.service.WithdrawalNoticeService;

import jakarta.validation.Valid;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/investors")
public class InvestorController {

    private final InvestorService investorService;
    private final ProductService productService;
    private final WithdrawalNoticeService withdrawalNoticeService;
    private final CSVGeneratorUtil csvGeneratorUtil;

    public InvestorController(
        InvestorService investorService,
        ProductService productService,
        WithdrawalNoticeService withdrawalNoticeService,
        CSVGeneratorUtil csvGeneratorUtil
    ) {
        this.investorService = investorService;
        this.productService = productService;
        this.withdrawalNoticeService = withdrawalNoticeService;
        this.csvGeneratorUtil = csvGeneratorUtil;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investor> getInvestor(@PathVariable Long id) {
        Investor investor = investorService.getInvestorById(id);
        return ResponseEntity.ok(investor);
    }
    
    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getInvestorProducts(@PathVariable Long id) {
        List<Product> products = productService.getInvestorProducts(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{investorId}/products/{productId}")
    public ResponseEntity<Product> getInvestorProductById(
            @PathVariable Long investorId,
            @PathVariable Long productId
    ) {
        Product product = productService.getInvestorProductById(investorId, productId);
        return ResponseEntity.ok(product);
    }
    
    @PostMapping("/{investorId}/products/{productId}/withdraw")
    public ResponseEntity<WithdrawalNotice> createWithdrawalNotice(
        @PathVariable Long investorId,
        @PathVariable Long productId,
        @Valid @RequestBody WithdrawalNoticeDTO withdrawalNoticeDTO
    ) {
        WithdrawalNotice withdrawalNotice = withdrawalNoticeService
            .createWithdrawalNotice(investorId, productId, withdrawalNoticeDTO);
        return new ResponseEntity<>( withdrawalNotice, HttpStatus.CREATED );
    }


    @GetMapping("/{investorId}/products/{productId}/statements")
    public ResponseEntity<byte[]> getProductWithdrawalNotices(
            @PathVariable Long investorId,
            @PathVariable Long productId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        if (start == null) {
            // default to three months before current date
            start = LocalDate.now()
                    .minusDays(LocalDate.now().getDayOfMonth())
                    .minusMonths(3);
        }

        if (end == null) { end = LocalDate.now(); }

        List<WithdrawalNotice> withdrawalNotices = withdrawalNoticeService
                .getProductWithdrawalNotices(investorId, productId, start, end);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyyMMdd" );

        String fileName = "withdrawal-"
                + (start.equals( end ) ? formatter.format( start )  :
                   String.format( "%s-%s", formatter.format( start ), formatter.format( end ) ))
                + ".csv";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_OCTET_STREAM );
        headers.setContentDispositionFormData("attachment", fileName);

        byte[] noticesCSVBytes = csvGeneratorUtil
                .generateCSVFromWithdrawalNotices(withdrawalNotices)
                .getBytes( StandardCharsets.UTF_8 );

        return new ResponseEntity<>(noticesCSVBytes, headers, HttpStatus.OK);
    }
}
