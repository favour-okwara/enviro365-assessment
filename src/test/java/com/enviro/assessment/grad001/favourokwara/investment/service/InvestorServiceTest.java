package com.enviro.assessment.grad001.favourokwara.investment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.enviro.assessment.grad001.favourokwara.investment.exception.ApplicationException;
import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.repository.InvestorRepository;

@ExtendWith(MockitoExtension.class)
public class InvestorServiceTest {
    @Mock
    private InvestorRepository investorRepository;

    @InjectMocks
    private InvestorService investorService;

    @Test
    public void InvestorService_GetInvestorById_ReturnsInvestor() {
        Long investorId = 1L;
        Investor investor = new Investor("James", "Bond", "jamesbnd007@email.com", "+12-34-567-8901", LocalDate.of(1960, 10, 17));

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));

        Investor result = investorService.getInvestorById(investorId);

        assertEquals(investor, result);
        verify(investorRepository, times(1)).findById(investorId);
    }

    @Test
    public void InvestorService_GetInvestorById_NonExistingInvestor() {
        Long investorId = 2L;

        when(investorRepository.findById(investorId)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> investorService.getInvestorById(investorId));
        verify(investorRepository, times(1)).findById(investorId);
    }
}
