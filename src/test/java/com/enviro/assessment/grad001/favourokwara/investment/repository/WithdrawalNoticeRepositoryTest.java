package com.enviro.assessment.grad001.favourokwara.investment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.enviro.assessment.grad001.favourokwara.investment.model.BankingDetails;
import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.model.Product;
import com.enviro.assessment.grad001.favourokwara.investment.model.ProductType;
import com.enviro.assessment.grad001.favourokwara.investment.model.WithdrawalNotice;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class WithdrawalNoticeRepositoryTest {

    @Autowired
    private WithdrawalNoticeRepository withdrawalNoticeRepository;

    @Autowired
    InvestorRepository investorRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void WithdrawalNoticeRepository_SaveAll_ReturnsNotice() {
        Investor investor = investorRepository.save(new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17)));

        Product product1 = new Product( "James' Retirement Fund", ProductType.RETIREMENT, 50000.0 );
        product1.setInvestor( investor );

        productRepository.save(product1);

        WithdrawalNotice notice1 = new WithdrawalNotice( 300.0, LocalDate.of( 2024, 1, 25 ), new BankingDetails( "FNB", "12345678910" ) );
        notice1.setProduct( product1 );

        WithdrawalNotice savedNotice = withdrawalNoticeRepository.save(notice1);

        assertNotNull(savedNotice);
        assertTrue(savedNotice.getId() > 0);
    }

    @Test
    public void WithdrawalNoticeRepository_FindByProductIdAndCreatedDateBetween_ReturnNotice() {
        Investor investor = investorRepository.save(new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17)));

        Product product1 = new Product( "James' Retirement Fund", ProductType.RETIREMENT, 50000.0 );
        product1.setInvestor( investor );

        productRepository.save(product1);

        WithdrawalNotice notice1 = new WithdrawalNotice( 300.0, LocalDate.of( 2024, 1, 25 ), new BankingDetails( "FNB", "12345678910" ) );
        notice1.setProduct( product1 );

        WithdrawalNotice notice2 = new WithdrawalNotice( 300.0, LocalDate.of( 2024, 1, 25 ), new BankingDetails( "FNB", "12345678910" ) );
        notice2.setProduct( product1 );

        withdrawalNoticeRepository.saveAll(List.of(notice1, notice2));

        List <WithdrawalNotice> createdNotices = withdrawalNoticeRepository.findByProductIdAndCreatedDateBetween( product1.getId(), LocalDate.now().atStartOfDay().minusDays( 1 ), LocalDate.now().atStartOfDay().plusDays( 1 ) );

        assertFalse( createdNotices.isEmpty() );
        assertEquals( 2, createdNotices.size() );
    }


    @Test
    public void WithdrawalNoticeRepository_FindByIdAndCreatedDateBetween_ReturnsEmptyList() {
        Long productId = 1L;
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 12, 31, 23, 59);

        List<WithdrawalNotice> withdrawalNotices = withdrawalNoticeRepository.findByProductIdAndCreatedDateBetween(
                productId, start, end);

        assertTrue(withdrawalNotices.isEmpty());
        assertEquals(0, withdrawalNotices.size());
    }
}
