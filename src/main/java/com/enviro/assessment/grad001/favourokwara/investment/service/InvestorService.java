package com.enviro.assessment.grad001.favourokwara.investment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.enviro.assessment.grad001.favourokwara.investment.exception.ApplicationException;
import com.enviro.assessment.grad001.favourokwara.investment.exception.Errors;
import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;
import com.enviro.assessment.grad001.favourokwara.investment.repository.InvestorRepository;

import lombok.NonNull;

@Service
public class InvestorService {

    private final InvestorRepository investorRepository;

    public InvestorService(InvestorRepository repository) {
        this.investorRepository = repository;
    }

    public Investor getInvestorById(@NonNull Long investorId) {
        return investorRepository
            .findById(investorId)
            .orElseThrow(() -> new ApplicationException(Errors.INVESTOR_NOT_FOUND, List.of(investorId)));
    }
}
