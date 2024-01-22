package com.enviro.assessment.grad001.favourokwara.investment.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

import com.enviro.assessment.grad001.favourokwara.investment.dto.WithdrawalNoticeDTO;
import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.ProductType;
import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;
import com.enviro.assessment.grad001.favourokwara.investment.repository.InvestorRepository;
import com.enviro.assessment.grad001.favourokwara.investment.repository.ProductRepository;
import com.enviro.assessment.grad001.favourokwara.investment.repository.WithdrawalNoticeRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InvestorRepository investorRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public ProductService(
        ProductRepository productRepository,
        InvestorRepository investorRepository,
        WithdrawalNoticeRepository withdrawalNoticeRepository
    ) {
        this.productRepository = productRepository;
        this.investorRepository = investorRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
        
    }

    public List<Product> getInvestorProducts(Long investorId) {
        Investor investor = investorRepository
            .findById(investorId)
            .orElseThrow(() -> new RuntimeException(String.format("%s with the %s of %s was not found","Investor", "id", investorId)));
        
        return investor.getProducts();
    }

    public WithdrawalNotice createWithdrawalNotice(WithdrawalNoticeDTO withdrawalDTO) {
        Product product = productRepository
            .findById(withdrawalDTO.getProductId())
            .orElseThrow(() -> new RuntimeException(new RuntimeException(String.format("%s with the %s of %s was not found","Product", "id", String.valueOf(1L)))));
        
        if ((product.getProductType().equals(ProductType.RETIREMENT) &&
              product.getInvestor().calculateAge() < 65)) {
            throw new RuntimeException("Investor should be older than 65");
        }

        if (withdrawalDTO.getAmount() > product.getBalance()) {
            throw new RuntimeException("Amount can't be greater than balance");
        }
        
        WithdrawalNotice createdNotice = withdrawalDTO.toWithdrawalNotice();
        createdNotice.setProduct(product);
        product.getWithdrawalNotices().add(createdNotice);
        
        withdrawalNoticeRepository.save(createdNotice);
        return createdNotice;
    }

    public List<WithdrawalNotice> getWithdrawalNoticesByProductId(Long produtId) {
        Product product = productRepository
            .findById(produtId)
            .orElseThrow(() -> new RuntimeException(String.format("%s with the %s of %d was not found","Product", "id", produtId)));

        return product.getWithdrawalNotices();
    }

    public List<WithdrawalNotice> getWithdrawalNoticesBetweenDates(
        Long productId,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        List<WithdrawalNotice> withdrawalNotices = getWithdrawalNoticesByProductId(productId);

        return withdrawalNotices.stream().filter(notice -> {
            LocalDateTime createdDate = notice.getCreatedDate();
            return createdDate.isAfter(startDate) && createdDate.isBefore(endDate);
        } ).toList();
    }
}
