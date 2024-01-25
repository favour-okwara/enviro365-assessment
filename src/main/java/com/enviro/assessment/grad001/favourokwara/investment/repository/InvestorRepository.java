package com.enviro.assessment.grad001.favourokwara.investment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enviro.assessment.grad001.favourokwara.investment.model.Investor;

/**
 * InvestorRepository interface responsible for managing Investor entities.
 * Extends JpaRepository to provide CRUD operation on our entities.
 */
@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {
}
