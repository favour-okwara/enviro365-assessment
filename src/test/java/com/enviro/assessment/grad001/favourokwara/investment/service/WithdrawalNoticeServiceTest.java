package com.enviro.assessment.grad001.favourokwara.investment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enviro.assessment.grad001.favourokwara.investment.dto.WithdrawalNoticeDTO;
import com.enviro.assessment.grad001.favourokwara.investment.exception.ApplicationException;
import com.enviro.assessment.grad001.favourokwara.investment.model.BankingDetails;
import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.ProductType;
import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;
import com.enviro.assessment.grad001.favourokwara.investment.repository.InvestorRepository;
import com.enviro.assessment.grad001.favourokwara.investment.repository.ProductRepository;
import com.enviro.assessment.grad001.favourokwara.investment.repository.WithdrawalNoticeRepository;

@ExtendWith(MockitoExtension.class)
public class WithdrawalNoticeServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    InvestorRepository investorRepository;

    @Mock
    WithdrawalNoticeRepository withdrawalNoticeRepository;

    @InjectMocks
    WithdrawalNoticeService withdrawalNoticeService;


    @Test
    public void WithdrawalNoticeService_CreateWithdrawalNotice_ReturnsWithdrawalNotice() {
        Investor investor = new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17));
        investor.setId( 1L );

        Product product1 = new Product( "James' Savings Fund", ProductType.SAVINGS, 50000.0 );
        product1.setId( 1L );
        product1.setInvestor( investor );

        WithdrawalNoticeDTO noticeDTO = new WithdrawalNoticeDTO( 300.0, "FNB", "12345678910", LocalDate.now().plusDays( 1 ) );

        when( investorRepository.findById( investor.getId()) ).thenReturn( Optional.of( investor ) ).thenReturn( Optional.of( investor ) );
        when( productRepository.findByIdAndInvestorId( product1.getId(), investor.getId() ) ).thenReturn( Optional.of( product1 ) );

        WithdrawalNotice result = withdrawalNoticeService.createWithdrawalNotice( investor.getId(), product1.getId(), noticeDTO);

        verify( investorRepository, times( 1 ) ).findById( investor.getId() );
        verify( productRepository, times( 1 ) ).findByIdAndInvestorId(  product1.getId(), investor.getId() );
        verify( withdrawalNoticeRepository, times( 1 ) ).save( Mockito.any(WithdrawalNotice.class) );
    }

    @Test
    public void WithdrawalNoticeService_CreateWithdrawalNotice_InvestorNotFound() {

        WithdrawalNoticeDTO noticeDTO = new WithdrawalNoticeDTO( 300.0, "FNB", "12345678910", LocalDate.now().plusDays( 1 ) );

        when( investorRepository.findById( Mockito.any( Long.class ) ) ).thenReturn( Optional.empty() );

        Long investorId = 1L;
        Long productId = 2L;
        assertThrows( ApplicationException.class, () -> withdrawalNoticeService.createWithdrawalNotice(investorId, productId, noticeDTO) );
    }

    @Test
    public void WithdrawalNoticeService_CreateWithdrawalNotice_ProductNotFound() {
        Long investorId = 1L;
        Long productId = 2L;

        Investor investor = new Investor( "James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of( 1960, 10, 17 ) );
        investor.setId( investorId );

        WithdrawalNoticeDTO noticeDTO = new WithdrawalNoticeDTO( 300.0, "FNB", "12345678910", LocalDate.now().plusDays( 1 ) );

        when( investorRepository.findById( investorId ) ).thenReturn( Optional.of(investor) );
        when( productRepository.findByIdAndInvestorId( productId, investor.getId() ) ).thenReturn( Optional.empty() );


        assertThrows( ApplicationException.class, () -> withdrawalNoticeService.createWithdrawalNotice(investorId, productId, noticeDTO) );
    }

    @Test
    public void WithdrawalNoticeService_CreateWithdrawalNotice_InvestorNotEligible() {
        Long investorId = 1L;
        Long productId = 2L;

        Investor investor = new Investor( "James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of( 2000, 10, 17 ) );
        investor.setId( investorId );

        // Can only withdraw from retirement if you're above 65
        Product product1 = new Product( "James' Retirement Fund", ProductType.RETIREMENT, 50000.0 );
        product1.setId( productId );
        product1.setInvestor( investor );

        WithdrawalNoticeDTO noticeDTO = new WithdrawalNoticeDTO( 300.0, "FNB", "12345678910", LocalDate.now().plusDays( 1 ) );

        when( investorRepository.findById( investorId ) ).thenReturn( Optional.of(investor) );
        when( productRepository.findByIdAndInvestorId( productId, investor.getId() ) ).thenReturn( Optional.of( product1 ) );

        assertThrows( ApplicationException.class, () -> withdrawalNoticeService.createWithdrawalNotice(investorId, productId, noticeDTO) );
    }

    @Test
    public void WithdrawalNoticeService_CreateWithdrawalNotice_InsufficientFunds() {
        Long investorId = 1L;
        Long productId = 2L;

        Investor investor = new Investor( "James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of( 2000, 10, 17 ) );
        investor.setId( investorId );

        Product product1 = new Product( "James' Savings Fund", ProductType.SAVINGS, 50000.0 );
        product1.setId( productId );
        product1.setInvestor( investor );

        // attempt to withdraw all the money from the product
        WithdrawalNoticeDTO noticeDTO = new WithdrawalNoticeDTO( 50000.0, "FNB", "12345678910", LocalDate.now().plusDays( 1 ) );

        when( investorRepository.findById( investorId ) ).thenReturn( Optional.of(investor) );
        when( productRepository.findByIdAndInvestorId( productId, investor.getId() ) ).thenReturn( Optional.of( product1 ) );

        assertThrows( ApplicationException.class, () -> withdrawalNoticeService.createWithdrawalNotice(investorId, productId, noticeDTO) );
    }

    @Test
    public void WithdrawalNoticeService_GetProductWithdrawalNotices_ReturnsWithdrawalNotices() {
        Long investorId = 1L;
        Long productId = 2L;

        Investor investor = new Investor( "James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of( 2000, 10, 17 ) );
        investor.setId( investorId );

        Product product1 = new Product( "James' Savings Fund", ProductType.SAVINGS, 50000.0 );
        product1.setId( productId );
        product1.setInvestor( investor );

        List<WithdrawalNotice> withdrawalNotices = new ArrayList<>();

        withdrawalNotices.add( new WithdrawalNotice(365.0, LocalDate.now().plusDays( 1 ), new BankingDetails("FNB", "10987654321")) );
        withdrawalNotices.add( new WithdrawalNotice(520.0, LocalDate.now().plusDays( 1 ), new BankingDetails("FNB", "12345678910")) );


        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
        when(productRepository.findByIdAndInvestorId(productId, investorId)).thenReturn(Optional.of(product1));
        when(withdrawalNoticeRepository.findByProductIdAndCreatedDateBetween(Mockito.any(Long.class), Mockito.any( LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(withdrawalNotices);

        List<WithdrawalNotice> result = withdrawalNoticeService.getProductWithdrawalNotices( investorId, productId, LocalDate.now().minusMonths( 3 ), LocalDate.now() );

        verify(investorRepository, times(1)).findById(investorId);
        verify(productRepository, times(1)).findByIdAndInvestorId(productId, investorId);
        verify(withdrawalNoticeRepository, times(1)).findByProductIdAndCreatedDateBetween(Mockito.any(Long.class), Mockito.any( LocalDateTime.class), Mockito.any(LocalDateTime.class) );

        assertFalse( withdrawalNotices.isEmpty() );
        assertEquals( 2, result.size() );
    }

    @Test
    public void WithdrawalNoticeService_GetProductWithdrawalNotices_InvalidDateRange() {
        Long investorId = 1L;
        Long productId = 2L;
        LocalDate startDate = LocalDate.now();
        LocalDate stopDate = LocalDate.now().minusDays(1); // Invalid date range

        assertThrows( ApplicationException.class, () -> withdrawalNoticeService.getProductWithdrawalNotices(investorId, productId, startDate, stopDate) );
    }

    @Test
    public void WithdrawalNoticeService_GetProductWithdrawalNotices_InvestorNotFound() {
        Long investorId = 1L;
        Long productId = 2L;
        LocalDate startDate = LocalDate.now();
        LocalDate stopDate = LocalDate.now().minusDays(1); // Invalid date range

        assertThrows( ApplicationException.class, () -> withdrawalNoticeService.getProductWithdrawalNotices(investorId, productId, startDate, stopDate) );
    }
}
