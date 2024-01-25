package com.enviro.assessment.grad001.favourokwara.investment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.enviro.assessment.grad001.favourokwara.investment.exception.ApplicationException;
import com.enviro.assessment.grad001.favourokwara.investment.exception.Errors;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.repository.InvestorRepository;
import com.enviro.assessment.grad001.favourokwara.investment.repository.ProductRepository;

import lombok.NonNull;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InvestorRepository investorRepository;

    public ProductService(
        ProductRepository productRepository,
        InvestorRepository investorRepository
    ) {
        this.productRepository = productRepository;
        this.investorRepository = investorRepository;
    }

    public List<Product> getInvestorProducts(@NonNull Long investorId) {
        investorRepository
            .findById(investorId) // ensure that the investor exists
            .orElseThrow(() -> new ApplicationException(Errors.INVESTOR_NOT_FOUND, List.of(investorId)));
        
        return productRepository.findAllByInvestorId(investorId);
    }

    public Product getInvestorProductById(@NonNull Long investorId, Long productId) {
        investorRepository
            .findById(investorId)
            .orElseThrow(() -> new ApplicationException(Errors.INVESTOR_NOT_FOUND, List.of(investorId)));

        return productRepository
                .findByIdAndInvestorId(productId, investorId)
                .orElseThrow(() -> new ApplicationException( Errors.PRODUCT_NOT_FOUND, List.of(productId)));
    }
}
