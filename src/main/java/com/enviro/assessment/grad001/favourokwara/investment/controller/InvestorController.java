package com.enviro.assessment.grad001.favourokwara.investment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enviro.assessment.grad001.favourokwara.investment.dto.WithdrawalNoticeDTO;
import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;
import com.enviro.assessment.grad001.favourokwara.investment.service.InvestorService;
import com.enviro.assessment.grad001.favourokwara.investment.service.ProductService;

import jakarta.validation.Valid;

import java.util.List;

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

    public InvestorController(
        InvestorService investorService,
        ProductService productService
    ) {
        this.investorService = investorService;
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investor> getInvestor(@PathVariable Long id) {
        Investor investor = investorService.getInvestorById(id);
        return ResponseEntity.ok(investor);
    }
    
    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getInevstorProducts(@PathVariable Long id) {
        List<Product> products = productService.getInvestorProducts(id);
        return ResponseEntity.ok(products);
    }
    
    @PostMapping("/{id}/withdrawal")
    public ResponseEntity<WithdrawalNotice> createWithdrawalNotice(
        @PathVariable Long id,
        @RequestBody @Valid WithdrawalNoticeDTO withdrawalNoticeDTO
    ) {
        WithdrawalNotice withdrawalNotice = productService
            .createWithdrawalNotice(id, withdrawalNoticeDTO);
        return ResponseEntity.ok(withdrawalNotice);
    }
    
}
