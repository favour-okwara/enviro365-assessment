package com.enviro.assessment.grad001.favourokwara.investment.service;

import org.springframework.stereotype.Service;

import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.repository.InvestorRepository;

@Service
public class InvestorService {

    private final InvestorRepository investorRepository;

    public InvestorService(InvestorRepository repository) {
        this.investorRepository = repository;
    }

    public Investor getInvestorById(Long investorId) {
        return investorRepository
            .findById(investorId)
            .orElseThrow(() -> new RuntimeException(String.format("%s with the %s of %s was not found","Investor", "id", investorId)));
    }
}
