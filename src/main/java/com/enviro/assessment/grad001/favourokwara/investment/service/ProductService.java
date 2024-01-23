package com.enviro.assessment.grad001.favourokwara.investment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.enviro.assessment.grad001.favourokwara.investment.dto.WithdrawalNoticeDTO;
import com.enviro.assessment.grad001.favourokwara.investment.exception.ApplicationException;
import com.enviro.assessment.grad001.favourokwara.investment.exception.Errors;
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
            .orElseThrow(() -> new ApplicationException(Errors.INVESTOR_NOT_FOUND, List.of(investorId)));
        
        return investor.getProducts();
    }

    public WithdrawalNotice createWithdrawalNotice(Long inevstorId, WithdrawalNoticeDTO withdrawalDTO) {

        Product product = getInvestorProducts(inevstorId)
            .stream()
            .filter(prod -> prod.getId().equals(withdrawalDTO.getProductId()))
            .findFirst()
            .orElseThrow(() -> new ApplicationException(Errors.PRODUCT_NOT_FOUND, List.of(withdrawalDTO.getProductId())));
        if ((product.getProductType().equals(ProductType.RETIREMENT) &&
            product.getInvestor().calculateAge() < 65)) {
            throw new ApplicationException(Errors.INVESTOR_NOT_ELIGIBLE);
        }

        if (withdrawalDTO.getAmount() > product.getBalance()) {
            throw new ApplicationException(Errors.INSUFFICIENT_FUNDS, List.of(product.getId()));
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
            .orElseThrow(() -> new ApplicationException(Errors.PRODUCT_NOT_FOUND, List.of(produtId)));

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

    public List<WithdrawalNotice> getWithdrawalNoticesBetweenDates(Long productId, LocalDate start, LocalDate stop) {

        List<WithdrawalNotice> withdrawalNotices = getWithdrawalNoticesByProductId(productId);

        return withdrawalNotices
            .stream()
            .filter(notice -> {
                LocalDateTime createDate = notice.getCreatedDate();

                return createDate.isAfter(start.atStartOfDay()) && 
                    createDate.isBefore(stop.plusDays(1).atStartOfDay());
        }).toList();
    }
}
