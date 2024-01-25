package com.enviro.assessment.grad001.favourokwara.investment.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.enviro.assessment.grad001.favourokwara.investment.dto.WithdrawalNoticeDTO;
import com.enviro.assessment.grad001.favourokwara.investment.exception.ApplicationException;
import com.enviro.assessment.grad001.favourokwara.investment.exception.Errors;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.ProductType;
import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;
import com.enviro.assessment.grad001.favourokwara.investment.repository.InvestorRepository;
import com.enviro.assessment.grad001.favourokwara.investment.repository.ProductRepository;
import com.enviro.assessment.grad001.favourokwara.investment.repository.WithdrawalNoticeRepository;

import lombok.NonNull;

@Service
public class WithdrawalNoticeService {

    private final ProductRepository productRepository;
    
    private final InvestorRepository investorRepository;
    
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public WithdrawalNoticeService (
        ProductRepository productRepository,
        InvestorRepository investorRepository,
        WithdrawalNoticeRepository withdrawalNoticeRepository
    ) {
        this.productRepository = productRepository;
        this.investorRepository = investorRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

        public WithdrawalNotice createWithdrawalNotice(@NonNull Long investorId, Long productId, WithdrawalNoticeDTO withdrawalDTO) {
        investorRepository
                .findById(investorId)
                .orElseThrow(() -> new ApplicationException(Errors.INVESTOR_NOT_FOUND, List.of(investorId)));

        Product product = productRepository
                .findByIdAndInvestorId(productId, investorId)
                .orElseThrow(() -> new ApplicationException(
                        Errors.PRODUCT_NOT_FOUND,
                        List.of(productId)
                ));

        if ((product.getProductType().equals(ProductType.RETIREMENT) &&
            product.getInvestor().calculateAge() < 65)) {
            throw new ApplicationException(Errors.INVESTOR_NOT_ELIGIBLE);
        }

        if (withdrawalDTO.getAmount() > product.getBalance() || (withdrawalDTO.getAmount() / product.getBalance()) > 0.9 ) {
            throw new ApplicationException(Errors.INSUFFICIENT_FUNDS, List.of(product.getId()));
        }
        
        WithdrawalNotice createdNotice = withdrawalDTO.toWithdrawalNotice();
        createdNotice.setProduct(product);

        return withdrawalNoticeRepository.save(createdNotice);
    }

    public List<WithdrawalNotice> getProductWithdrawalNotices(
            @NonNull Long investorId,
            @NonNull Long productId,
            LocalDate startDate,
            LocalDate stopDate
    ) {
        if (startDate.isAfter( stopDate )) {
            throw new ApplicationException( Errors.INVALID_DATE_RANGE );
        }

        investorRepository
                .findById(investorId)
                .orElseThrow(() -> new ApplicationException(Errors.INVESTOR_NOT_FOUND, List.of(investorId)));

        productRepository
                .findByIdAndInvestorId(productId, investorId)
                .orElseThrow(() -> new ApplicationException(
                        Errors.PRODUCT_NOT_FOUND, List.of(productId)
                ));

        return withdrawalNoticeRepository.findByProductIdAndCreatedDateBetween(
                productId,
                startDate.atStartOfDay(),
                stopDate.atStartOfDay().plusDays( 1 )
        );
    }
}
