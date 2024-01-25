package com.enviro.assessment.grad001.favourokwara.investment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.ProductType;


@DataJpaTest

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTest {

    @Autowired
    InvestorRepository investorRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void ProductRepositoryTest_FindAllByInvestorId_ReturnsProducts() {
        Long productId = 1L;

        Investor investor = investorRepository.save(new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17)));
        
        Product product = new Product("Savings", ProductType.SAVINGS, 50000.0);
        product.setInvestor(investor);

        productRepository.save(product);

        Optional<Product> result = productRepository.findByIdAndInvestorId(product.getId(), investor.getId());

        assertTrue(result.isPresent());
        assertEquals(product.getId(), result.get().getId());
    }

    @Test
    public void ProductRepositoryTest_FindByIdAndInvestorId_ReturnsProducts() {
        Long productId = 1L;
    
        Investor investor = investorRepository.save(new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17)));
        
        Product product = new Product("Savings", ProductType.SAVINGS, 50000.0);
        product.setInvestor(investor);
    
        Product savedProduct = productRepository.save(product);
    
        Optional<Product> result = productRepository.findByIdAndInvestorId(product.getId(), investor.getId());
    
        assertTrue(result.isPresent());
        assertEquals(savedProduct.getId(), result.get().getId());
        assertEquals(investor.getId(), result.get().getInvestor().getId());
        assertEquals(savedProduct.getName(), result.get().getName());
    }

    @Test
    public void ProductRepositoryTest_FindByIdAndInvestorId_ReturnsEmptyOptionalWhenNotFound() {
        Long investorId = 1L;
        Long productId = 1L;

        Optional<Product> result = productRepository.findByIdAndInvestorId(productId, investorId);

        assertTrue(result.isEmpty());
    }
}