package com.enviro.assessment.grad001.favourokwara.investment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enviro.assessment.grad001.favourokwara.investment.exception.ApplicationException;
import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.ProductType;
import com.enviro.assessment.grad001.favourokwara.investment.repository.InvestorRepository;
import com.enviro.assessment.grad001.favourokwara.investment.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    InvestorRepository investorRepository;

    @InjectMocks
    ProductService productService;

    @Test
    public void ProductService_GetInvestorProducts_ReturnsManyProducts() {
        Investor investor = new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17));
        investor.setId( 1L );

        Product product1 = new Product( "James' Retirement Fund", ProductType.RETIREMENT, 50000.0 );
        product1.setId( 1L );
        product1.setInvestor( investor );

        Product product2 = new Product( "James' Savings Fund", ProductType.SAVINGS, 43000.0 );
        product2.setId( 2L );
        product2.setInvestor( investor );

        when( investorRepository.findById( Mockito.any( Long.class) ) ).thenReturn( Optional.of( investor ) );
        when( productRepository.findAllByInvestorId( 1L ) ).thenReturn( List.of(product1, product2) );

        List<Product> result = productService.getInvestorProducts( investor.getId() );

        assertEquals( 2, result.size() );
        assertEquals( 1L, result.get( 0 ).getId() );
        assertEquals( 2L, result.get( 1 ).getId() );
    }

    @Test public void ProductService_GetInvestorProducts_InvestorsNotFound() {
        when( investorRepository.findById( Mockito.any( Long.class) ) ).thenReturn( Optional.empty() );

        assertThrows( ApplicationException.class, () -> productService.getInvestorProducts( 1L ) );

        // verify methods we called n times
        verify( investorRepository, times( 1 ) ).findById( Mockito.any( Long.class) );
    }

    @Test public void ProductService_GetInvestorProductById_ReturnsProducts() {
        Investor investor = new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17));
        investor.setId( 1L );

        Product product1 = new Product( "James' Retirement Fund", ProductType.RETIREMENT, 50000.0 );
        product1.setId( 1L );
        product1.setInvestor( investor );

        when( investorRepository.findById( Mockito.any( Long.class) ) ).thenReturn( Optional.of( investor ) );
        when( productRepository.findByIdAndInvestorId( 1L, 1L ) ).thenReturn( Optional.of( product1 ) );

        Product result = productService.getInvestorProductById( 1L, 1L );

        verify( investorRepository, times( 1 ) ).findById( investor.getId() );
        verify( productRepository, times( 1 ) ).findByIdAndInvestorId( product1.getId(), investor.getId() );
        assertNotNull( result );
    }

    @Test
    public void ProductService_GetInvestorProductById_InvestorNotFound() {
        when( investorRepository.findById( Mockito.any( Long.class) ) ).thenReturn( Optional.empty() );

        Long investorId = 2L;
        Long productId = 1L;
        assertThrows( ApplicationException.class, () -> productService.getInvestorProductById(investorId, productId) );
    }

    @Test
    public void ProductService_GetInvestorProductById_ProductNotFound() {
        Investor investor = new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17));
        investor.setId( 1L );

        Long productId = 1L;

        when( investorRepository.findById( investor.getId() ) ).thenReturn( Optional.of( investor ) );
        when( productRepository.findByIdAndInvestorId( productId, investor.getId() ) ).thenReturn( Optional.empty() );

        assertThrows( ApplicationException.class, () -> productService.getInvestorProductById( investor.getId(), productId ) );
    }
}
