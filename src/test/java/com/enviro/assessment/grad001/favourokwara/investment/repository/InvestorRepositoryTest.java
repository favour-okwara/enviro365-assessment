package com.enviro.assessment.grad001.favourokwara.investment.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class InvestorRepositoryTest {

    @Autowired
    InvestorRepository investorRepository;

    // @Transactional
    @Test
    public void InvestorRepository_SaveAll_ReturnsInvestor() {
        Investor investor = new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17));

        Investor savedInvestor = investorRepository.save(investor);

        assertNotNull( savedInvestor );
        assertTrue( savedInvestor.getId() > 0 ); // Id is generated on save.
    }

    // @Transactional
    @Test
    public void InvestorRepository_FindById_ReturnsInvestor() {
        Investor saved = investorRepository.save(
            new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17)));

        Investor result = investorRepository.findById(saved.getId()).get();

        assertNotNull(result);
        assertTrue(result.getId() > 0);
    }
}
